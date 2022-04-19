package com.datapipeline.operator.multiple;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

/**
 * 本案例主要探讨flink的多流算子--- connect算子
 * connect用于连接2个流，这2个流可以是不同的类型，然后通过2个算子对流中的元素进行不同的处理。
 * Connect合并后，可用map中的CoMapFunction或flatMap中的CoFlatMapFunction来对合并流中的每个流进行处理。
 * connect可以单独使用，也可以配合Broadcast机制一起使用。
 * connect的主要使用场景是一个主流（数据流）和一个辅助流（比如配置、规则）连接，通过辅助流动态的控制主流的行为。
 * 特点：
 *  1，只能连接两条流
 *  2，两条流的类型可以不同
 *  3,connect 之后，可以用CoXXX算子
 */
public class ConnectStream {
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
        dataStream.print("主流数据：");
        // 规则数据流 模拟随着时间的推移规则在不断变化。
        DataStream<String> ruleStream = env.addSource(new SourceFunction<String>() {
            @Override
            public void run(SourceContext<String> ctx) throws Exception {
                ctx.collect("one");
                Thread.sleep(10000);
                ctx.collect("two");
                Thread.sleep(5000);
                ctx.collect("three");
                Thread.sleep(Long.MAX_VALUE);
            }

            @Override
            public void cancel() {
            }
        });
       ruleStream.print("规则流数据：");
        dataStream.connect(ruleStream)
                .flatMap(new CoFlatMapFunction<Long, String, Object>() {
                    String rule;
                    @Override
                    public void flatMap1(Long value, Collector<Object> out) throws Exception {
                        if ("one".equalsIgnoreCase(rule)) {
                            out.collect(value);
                        } else if ("two".equalsIgnoreCase(rule) && (value % 2 == 0)) {
                            out.collect(value);
                        } else if ("three".equalsIgnoreCase(rule) && (value % 3 == 0)) {
                            out.collect(value);
                        } else {
                            System.out.println("规则不匹配");
                        }
                    }
                    @Override
                    public void flatMap2(String rulevalue, Collector<Object> out) throws Exception {
                        System.out.printf("update rule, old rule = %s, new rule = %s\n", rule, rulevalue);
                        rule = rulevalue;
                    }
                }).print();

        env.execute("ConnectStream");
    }
}
