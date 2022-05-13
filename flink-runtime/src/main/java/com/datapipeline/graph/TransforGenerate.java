package com.datapipeline.graph;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class TransforGenerate {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        senv.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        DataStreamSource<String> text = senv.socketTextStream("127.0.0.1", 9999);
        text
                .flatMap(new LineSplitter()).shuffle()
                .filter(new WordFilter())
                .keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
                    @Override
                    public String getKey(Tuple2<String, Integer> value) throws Exception {
                        return value.f0;
                    }
                })
                .timeWindow(Time.seconds(5))
                .sum(1)
                .print();
        senv.execute();
//        System.out.println(senv.getExecutionPlan());
    }
}
class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
    @Override
    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
        String[] words = s.split(",");
        for(String word : words){
            collector.collect(Tuple2.of(word,1));
        }
    }
}
class WordFilter implements FilterFunction<Tuple2<String ,Integer>> {
    @Override
    public boolean filter(Tuple2<String, Integer> tuple2) throws Exception {
        return !StringUtils.isBlank(tuple2.f0);
    }

}
