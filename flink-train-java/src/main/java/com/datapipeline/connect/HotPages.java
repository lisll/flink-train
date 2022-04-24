package com.datapipeline.connect;

import com.datapipeline.model.ApacheLogEvent;
import com.datapipeline.model.PageViewCount;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.guava30.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class HotPages {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
        senv.setParallelism(1);
        senv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        // 从文件中读数据
//        URL resource = HotPages.class.getResource("/Users/sll/develop/workspace_idea/flink/UserBehaviorAnalysis/NetworkFlowAnalysis/src/main/resources/apache.log");
//        DataStream<String> inputStream = senv.readTextFile(resource.toString());
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"n1:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"consumer_test");
        String topic = "pageView";  //hotitems

//        FlinkKafkaConsumer flinkKafkaConsumer = new FlinkKafkaConsumer(topic, new CustomerDeserializationSchema(), properties);

        FlinkKafkaConsumer flinkKafkaConsumer = new FlinkKafkaConsumer(topic, new SimpleStringSchema(), properties);
        flinkKafkaConsumer.setStartFromEarliest();   // 从头开始消费

        DataStream dataStreamSource = senv.addSource(flinkKafkaConsumer);

//        DataStreamSource<String> inputStream = senv.socketTextStream("localhost", 7777);
//        inputStream.print("data");
        DataStream<ApacheLogEvent> mapStream = dataStreamSource.map(new MapFunction<String, ApacheLogEvent>() {
            @Override
            public ApacheLogEvent map(String line) throws Exception {
                String[] fields = line.split(" ");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
                Long timestamp = simpleDateFormat.parse(fields[3]).getTime();
                return new ApacheLogEvent(fields[0], fields[1], timestamp, fields[5], fields[6]);
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<ApacheLogEvent>(Time.seconds(1)) {
            @Override
            public long extractTimestamp(ApacheLogEvent apacheLogEvent) {
                return apacheLogEvent.getTimestamp();
            }
        });

        OutputTag<ApacheLogEvent> outputTag = new OutputTag<ApacheLogEvent>("late"){};
        SingleOutputStreamOperator<PageViewCount> windAggreStream = mapStream.filter(line -> "GET".equals(line.getMethod())).
                keyBy(ApacheLogEvent::getUrl).
                timeWindow(Time.of(10, TimeUnit.MINUTES), Time.of(5, TimeUnit.SECONDS)).
                allowedLateness(Time.minutes(1)).
                sideOutputLateData(outputTag).
                aggregate(new HotPageAggregate(), new HotPageWindowFunction());

//        windAggreStream.print("result");
         windAggreStream.getSideOutput(outputTag).print("lates");

        DataStream<String> topN  = windAggreStream.keyBy(PageViewCount::getWindowEnd).process(new HotPageTopN(3));
        // 本地调试
//        topN.print();
        // 输出到数据库
          topN.addSink(new MysqlSinkFunction());
        senv.execute("HotPages");
    }

    public static class HotPageAggregate implements AggregateFunction<ApacheLogEvent,Long,Long> {

        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(ApacheLogEvent apacheLogEvent, Long aLong) {
            return aLong+1;
        }

        @Override
        public Long getResult(Long aLong) {
            return aLong;
        }

        @Override
        public Long merge(Long a, Long b) {
            return a+b;
        }
    }

    public static class HotPageWindowFunction implements WindowFunction<Long,PageViewCount,String, TimeWindow>{

        @Override
        public void apply(String url, TimeWindow timeWindow, Iterable<Long> iterable, Collector<PageViewCount> collector) throws Exception {
                collector.collect(new PageViewCount(url,timeWindow.getEnd(),iterable.iterator().next()));
        }
    }

    private static class HotPageTopN extends KeyedProcessFunction<Long,PageViewCount, String> {
            int topN;
        public HotPageTopN(int topN) {
            this.topN = topN;
        }

//        private transient ListState<PageViewCount> listState;
        private transient MapState<String,Long> mapState;

        @Override
        public void open(Configuration parameters) throws Exception {
//            listState =   getRuntimeContext().getListState(new ListStateDescriptor<PageViewCount>("listState", TypeInformation.of(PageViewCount.class)));
            mapState = getRuntimeContext().getMapState(new MapStateDescriptor<String, Long>("mapState",String.class,Long.class));
        }



        @Override
        public void processElement(PageViewCount pageViewCount, KeyedProcessFunction<Long, PageViewCount, String>.Context context, Collector<String> collector) throws Exception {

//            listState.add(pageViewCount);
            mapState.put(pageViewCount.getUrl(),pageViewCount.getCount());
            context.timerService().registerEventTimeTimer(pageViewCount.getWindowEnd()+1);
            // 这个定时器主要是为了一分钟后清空状态的（因为上面设置了允许数据迟到1分钟）
            context.timerService().registerEventTimeTimer(pageViewCount.getWindowEnd()+60*1000L);
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            // 因为我们在上面注册了两个定时器，所以对定时器的触发时机要进行控制
            if(timestamp == ctx.getCurrentKey()+60*1000L){
                mapState.clear();
                return;
            }

            ArrayList<Map.Entry<String, Long>> pageViewCounts = Lists.newArrayList(mapState.entries());
            pageViewCounts.sort(new Comparator<Map.Entry<String, Long>>() {
                @Override
                public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                    if(o1.getValue()>o2.getValue()){
                        return -1;
                    }else if(o1.getValue()<o2.getValue()){
                        return 1;
                    }else{
                        return 0;
                    }
                }
            });


            // 格式化成String输出
            StringBuilder resultBuilder = new StringBuilder();
            resultBuilder.append("===================================\n");
            resultBuilder.append("窗口结束时间：").append(new Timestamp(timestamp - 1)).append("\n");

            // 遍历列表，取top n输出
            for (int i = 0; i < Math.min(topN, pageViewCounts.size()); i++) {
                Map.Entry<String, Long> entry = pageViewCounts.get(i);
                resultBuilder.append("NO ").append(i + 1).append(":")
                        .append(" 页面URL = ").append(entry.getKey())
                        .append(" 浏览量 = ").append(entry.getValue())
                        .append("\n");
            }
            resultBuilder.append("===============================\n\n");

            // 控制输出频率
            Thread.sleep(1000L);

            out.collect(resultBuilder.toString());


        }
    }
}
