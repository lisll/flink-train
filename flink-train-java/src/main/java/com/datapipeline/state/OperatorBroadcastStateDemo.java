package com.datapipeline.state;

import com.datapipeline.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.scala.typeutils.Types;
import org.apache.flink.streaming.api.datastream.BroadcastConnectedStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

public class OperatorBroadcastStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        // 数据流
        SingleOutputStreamOperator<WaterSensor> sensorDS = env.socketTextStream("127.0.0.1", 7777).map((MapFunction<String, WaterSensor>) value -> {
            String[] split = value.split(",");
            return new WaterSensor(split[0],Long.parseLong(split[1]),Integer.parseInt(split[2]));
        });

        // 配置流（用来广播配置）
        DataStream<String> configDS = env.socketTextStream("127.0.0.1", 8888);
        MapStateDescriptor<String, Integer> mapStateDescriptor = new MapStateDescriptor<>("broadcast-state", Types.STRING(), Types.INT());
        // TODO 1. 将 配置流 广播
        BroadcastStream<String> broadcast = configDS.broadcast(mapStateDescriptor);
        // TODO 2.把 数据流 和 广播后的配置流 connect
        BroadcastConnectedStream<WaterSensor, String> connect = sensorDS.connect(broadcast);
        // TODO 3.调用 process
        connect.process(new BroadcastProcessFunction<WaterSensor, String, String>() {

            //数据流的处理方法： 数据流 只能 读取 广播状态，不能修改
            @Override
            public void processElement(WaterSensor value, BroadcastProcessFunction<WaterSensor, String, String>.ReadOnlyContext ctx, Collector<String> collector) throws Exception {
                // TODO 5.通过上下文获取广播状态，取出里面的值（只读，不能修改）
                ReadOnlyBroadcastState<String, Integer> broadcastState = ctx.getBroadcastState(mapStateDescriptor);
                Integer threshold = broadcastState.get("threshold");
               // 判断广播状态里是否有数据，因为刚启动时，可能是数据流的第一条数据先来
                threshold = (threshold == null ? 0 : threshold);
                if(value.getVc() > threshold){
                    collector.collect(value + ",水位超过指定的阈值：" + threshold + "!!!");
                }

            }

            // 广播后的配置流的处理方法:  只有广播流才能修改广播状态
            @Override
            public void processBroadcastElement(String value, BroadcastProcessFunction<WaterSensor, String, String>.Context context, Collector<String> collector) throws Exception {
                // TODO 4. 通过上下文获取广播状态，往里面写数据
                BroadcastState<String, Integer> broadcastState = context.getBroadcastState(mapStateDescriptor);
                broadcastState.put("threshold",Integer.valueOf(value));
            }
        }).print();
        env.execute();
    }
}
