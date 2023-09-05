package com.datapipeline.processFunc;

import com.datapipeline.bean.WaterSensor;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * nc -l 7777
 */
public class KeyedProcessTimerDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<String> socketTextStream = env.socketTextStream("127.0.0.1", 7777);
        SingleOutputStreamOperator<WaterSensor> sensorDS  = socketTextStream.map(new MapFunction<String, WaterSensor>() {
            @Override
            public WaterSensor map(String value) throws Exception {
                String[] split = value.split(",");
                return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
            }
        }).assignTimestampsAndWatermarks(
                WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>)
                                (element, recordTimestamp) -> element.getTs()*1000));

        KeyedStream<WaterSensor, String> sensorKS = sensorDS.keyBy((KeySelector<WaterSensor, String>) sensor -> sensor.getId());
        sensorKS.process(new MyKeyProcessFunction()).print();

        env.execute();
    }
    public static class MyKeyProcessFunction extends KeyedProcessFunction<String,WaterSensor,String>{


        //来一条数据调用一次
        @Override
        public void processElement(WaterSensor waterSensor, KeyedProcessFunction<String, WaterSensor, String>.Context ctx, Collector<String> collector) throws Exception {
            //获取当前数据的key
            String currentKey = ctx.getCurrentKey();
            //注册定时器
            TimerService timerService = ctx.timerService();
            timerService.registerEventTimeTimer(5000L);
            // 获取事件时间
            Long currentEventTime = ctx.timestamp();
            System.out.println("当前key=" + currentKey + ",当前时间=" + currentEventTime + ",注册了一个5s的定时器");
            long currentWatermark = timerService.currentWatermark();
            System.out.println("当前数据=" + waterSensor + ",当前watermark=" + currentWatermark);
        }

        /**
         * 时间进展到定时器注册的时间，调用该方法
         * 当前时间进展，就是定时器被触发时的时间
         * @param timestamp
         * @param ctx
         * @param out
         * @throws Exception
         */
        @Override
        public void onTimer(long timestamp, KeyedProcessFunction<String, WaterSensor, String>.OnTimerContext ctx, Collector<String> out) throws Exception {
            super.onTimer(timestamp, ctx, out);
            String currentKey = ctx.getCurrentKey();
            System.out.println("key=" + currentKey + "现在时间是" + timestamp + "定时器触发");
        }
    }
}
