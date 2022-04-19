package com.datapipeline.operator.multiple;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 本案例主要探讨flink多流算子---union算子
 * union用于将多个、同类型的流合并成一个新的流。
 *  特点：
 *      1，可以合并多个流（2个及以上）
 *      2，多流的类型必须一样
 */
public class UnionStream {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
        senv.setParallelism(1);
        /**输入数据源source1*/
        DataStreamSource<Tuple3<String, String, String>> source1 = senv.fromElements(new Tuple3<>("productID1", "click", "user_1"));
        /**输入数据源source2*/
        DataStreamSource<Tuple3<String,String,String>> source2 = senv.fromElements(new Tuple3<>("1","1","1"));
        /**输入数据源source3*/
        DataStreamSource<Tuple3<String, String, String>> source3 = senv.fromElements(
                new Tuple3<>("productID2", "browse", "user_1"),
                new Tuple3<>("productID2", "click", "user_2"),
                new Tuple3<>("productID2", "click", "user_1")
        );
        //Union可以将两个或多个同数据类型的流合并成一个流。
        DataStream<Tuple3<String, String, String>> union = source1.union(source2,source3);

        union.print();
        senv.execute("UnionStream");
    }

}
