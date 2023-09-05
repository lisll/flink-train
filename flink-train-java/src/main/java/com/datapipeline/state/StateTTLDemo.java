package com.datapipeline.state;

import com.datapipeline.bean.WaterSensor;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class StateTTLDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<WaterSensor> sensorDS = env.socketTextStream("127.0.0.1", 7777).map((MapFunction<String, WaterSensor>) value -> {
            String[] split = value.split(",");
            return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
        }).assignTimestampsAndWatermarks(WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness
                        (Duration.ofSeconds(3))
                .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>)
                        (element, recordTimestamp) -> element.getTs() * 1000));
        sensorDS.keyBy(r -> r.getId()).process(new KeyedProcessFunction<String, WaterSensor, String>() {
            ValueState<Integer> lastVcState;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                StateTtlConfig stateTtlConfig = StateTtlConfig
                        .newBuilder(Time.seconds(5))
                        .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                        .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                        .build();
                ValueStateDescriptor<Integer> valueStateDescriptor = new ValueStateDescriptor<>("lastVcState", Types.INT);
                valueStateDescriptor.enableTimeToLive(stateTtlConfig);
                lastVcState = getRuntimeContext().getState(valueStateDescriptor);
            }

            @Override
            public void processElement(WaterSensor value, KeyedProcessFunction<String, WaterSensor, String>.Context context, Collector<String> out) throws Exception {
                // 先获取状态值，打印 ==》 读取状态
                Integer lastVc = lastVcState.value();
                out.collect("key=" + value.getId() + ",状态值=" + lastVc);
                // 如果水位大于10，更新状态值 ===》 写入状态
                if(value.getVc()>10){
                lastVcState.update(value.getVc());
                }
            }
        }).print();
        env.execute();
    }
}
