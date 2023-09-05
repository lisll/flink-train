package com.datapipeline.state;

import com.datapipeline.bean.WaterSensor;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.scala.typeutils.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * nc -lk 7777
 * 输入：
 * s1,1,1
 * s1,1,11
 * s1,1,12
 * s1,1,23
 */
public class KeyedValueStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<WaterSensor> sensorDS = env.socketTextStream("127.0.0.1", 7777)
                .map((MapFunction<String, WaterSensor>) value -> {
                    String[] split = value.split(",");
                    return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
                }).assignTimestampsAndWatermarks(
                        WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>)
                                        (element, recordTimestamp) -> element.getTs() * 1000));

        sensorDS.keyBy(r -> r.getId()).process(new KeyedProcessFunction<String, WaterSensor, String>() {
            // TODO 1.定义状态
            ValueState<Integer> lastVcState;

            // 2.在open方法中，初始化状态,状态描述器两个参数：第一个参数，起个名字，不重复；第二个参数，存储的类型
            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                lastVcState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("lastVcState", Types.INT()));
            }

            @Override
            public void processElement(WaterSensor value, KeyedProcessFunction<String, WaterSensor, String>.Context context, Collector<String> out) throws Exception {
                // 1. 取出上一条数据的水位值(Integer默认值是null，判断)
                Integer lastVc = lastVcState.value() == null ? 0 : lastVcState.value();
                //  2. 求差值的绝对值，判断是否超过10
                Integer nowVc = value.getVc();
                if (Math.abs(nowVc - lastVc) > 10) {
                    out.collect("传感器=" + value.getId() + "==>当前水位值=" + nowVc + ",与上一条水位值=" + lastVc + ",相差超过10！！！！");
                }
                // 3. 更新状态里的水位值
                lastVcState.update(nowVc);
            }
        }).print();
        env.execute();
    }
}
