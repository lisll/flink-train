package com.datapipeline.operator.multiple;

import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.BroadcastConnectedStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

public class ConnectStreamByBroadcas {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 主数据流
        DataStream<Long> dataStream = env.addSource(new SourceFunction<Long>() {
            @Override
            public void run(SourceContext<Long> ctx) throws Exception {
                long i = 0;
                while (true) {
                    ctx.collect(i++);
                    Thread.sleep(500);
                }
            }

            @Override
            public void cancel() {
            }
        });

        // 规则数据流
        DataStream<String> ruleStream = env.addSource(new SourceFunction<String>() {
            @Override
            public void run(SourceContext<String> ctx) throws Exception {
                ctx.collect("one");
                Thread.sleep(5000);
                ctx.collect("two");
                Thread.sleep(5000);
                ctx.collect("three");
                Thread.sleep(Long.MAX_VALUE);
            }

            @Override
            public void cancel() {
            }
        });

        // 这一段代码没搞明白
       MapStateDescriptor<Integer,Long> mapStateDescriptor = new MapStateDescriptor<>("mapState",
               TypeInformation.of(new TypeHint<Integer>() {}),
               BasicTypeInfo.LONG_TYPE_INFO);
        BroadcastStream<String> ruleBroadcastStream = ruleStream.broadcast(mapStateDescriptor);
        BroadcastConnectedStream<Long, String> connect = dataStream.connect(ruleBroadcastStream);
        connect.process(new BroadcastProcessFunction<Long, String, Long>() {
            String rule;
            @Override
            public void processElement(Long value, BroadcastProcessFunction<Long, String, Long>.ReadOnlyContext ctx, Collector<Long> out) throws Exception {
                if("one".equalsIgnoreCase(rule)){
                    out.collect(value);
                }else if("two".equalsIgnoreCase(rule) && (value % 2 ==0)){
                    out.collect(value);
                }else if("three".equalsIgnoreCase(rule) && (value % 3 ==0)){
                    out.collect(value);
                }else{
                    System.out.println("规则不匹配");
                }
            }

            @Override
            public void processBroadcastElement(String value, BroadcastProcessFunction<Long, String, Long>.Context ctx, Collector<Long> out) throws Exception {
                System.out.printf("update rule, old rule = %s, new rule = %s\n", rule, value);
                rule = value;
            }
        }).print();
        env.execute("ConnectStreamByBroadcas");
    }
}
