package com.datapipeline.streamAndBatch;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.hive.shaded.parquet.io.ValidatingRecordConsumer;
import org.apache.flink.orc.OrcFilters;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import javax.swing.plaf.TableUI;
import java.util.Arrays;
import java.util.Locale;

public class BatchDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataSource<String> text = env.fromElements(
                "Who's there?",
                "I think I hear them. Stand, ho! Who's there?",
                "li wen tao li wen tao li wen tao"
        );

        FlatMapOperator<String, Tuple2<String, Long>> map = text.flatMap((FlatMapFunction<String, Tuple2<String, Long>>) (s, collector) -> {
            String[] splits = s.toUpperCase().split("\\W+");
            for (String split : splits) {
                collector.collect(Tuple2.of(split, 1L));
            }
        }).returns(new TypeHint<Tuple2<String, Long>>() {
        });

        AggregateOperator<Tuple2<String, Long>> sum = map.groupBy(0).sum(1);
        sum.writeAsText("/Users/sll/develop/flink/flink-train/flink-train/flink-train-java/target/wordcount.txt", FileSystem.WriteMode.OVERWRITE);
//        System.out.println(env.getExecutionPlan());
        env.execute("this is jobname");
//        FlatMapOperator<String, Tuple2<String, Long>> map = text.flatMap((FlatMapFunction<String, Tuple2<String, Long>>)(s, collector) -> {
//            String[] splits = s.toUpperCase().split("\\W+");
//            for (String split : splits) {
//                collector.collect(Tuple2.of(split, 1L));
//            }
//        }).returns(new TypeHint<Tuple2<String,Long>>() {});
//        FlatMapOperator<String, Tuple2<String,Integer>> returns = text.flatMap((s, collector) -> {
//            String[] splits = s.toUpperCase().split("\\W+");
//            for (String split : splits) {
//                collector.collect(new Tuple2<>(split, 1));
//            }
//        }).returns(TypeInformation.of(new TypeHint<Tuple2<String,Integer>>() {
//        }));

//        AggregateOperator<Tuple2<String, Integer>> sum = map.groupBy(0).sum(1);
//        sum.print();
//        text.flatMap { _.toLowerCase.split("\\W+").filter{ _.nonEmpty } }
//      .map { (_, 1) }
//      .groupBy(0)
//                .sum(1)
//                .writeAsText

        // 1. 创建Flink 流式处理的核心环境类对象.
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        // 2. 通过核心类对象, 添加 数据源组件.
//        DataStreamSource<String> ds = env.socketTextStream("hadoop102", 7777);
//
//        //3. 在数据源组件中, 设置转换数据操作, 即: 实时计算操作.
//        SingleOutputStreamOperator<Tuple2<String, Long>> final_data = ds.flatMap(
//                (String line, Collector<Tuple2<String, Long>> out) -> {
//                    String[] words = line.split(" ");
//                    for (String word : words) {
//                        out.collect(Tuple2.of(word, 1L));
//                    }
//                }
//        ).returns(new TypeHint<Tuple2<String,Long>>() {}).keyBy(data -> data.f0).sum(1);
//
//        //4. 在转换后, 添加输出组件, 对统计结果进行输出.
//        final_data.print();
        //5. 启动执行Flink
//        env.execute();
    }
}
