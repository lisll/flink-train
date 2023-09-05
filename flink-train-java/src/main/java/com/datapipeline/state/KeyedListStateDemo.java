package com.datapipeline.state;

import com.datapipeline.bean.WaterSensor;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.scala.typeutils.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.hadoop.util.Waitable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KeyedListStateDemo {
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
        sensorDS.keyBy(r->r.getId()).process(new KeyedProcessFunction<String, WaterSensor, String>() {
            ListState<Integer> vcListState;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                vcListState = getRuntimeContext().getListState(new ListStateDescriptor<>("vcListState", Types.INT()));
            }

            @Override
            public void processElement(WaterSensor value, KeyedProcessFunction<String, WaterSensor, String>.Context context, Collector<String> out) throws Exception {
                vcListState.add(value.getVc());
                Iterable<Integer> vcListIt  = vcListState.get();
                List<Integer> vcList = new ArrayList<>();
                for (Integer integer : vcListIt) {
                    vcList.add(integer);
                }
                vcList.sort((o1, o2) -> o2-o1); // 降序
                if(vcList.size()>3){
                    vcList.remove(3);
                }
                out.collect("传感器id为" + value.getId() + ",最大的3个水位值=" + vcList.toString());
                vcListState.update(vcList);

            }
        }).print();
        env.execute();
    }
}
