package com.datapipeline.connect;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.util.Collector;

// 从hdfs上读取文件，处理后再写入到hdfs上
// 要将服务器上Hadoop安装目录下的 core-site.xml 和 hdfs-site.xml 放在当前项目的resources目录下
public class ReadWriteHdfs {

  public static void main(String[] args) throws Exception {
      ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
      env.setParallelism(1);
      DataSource<String> text = env.readTextFile("hdfs://n1:9000/test-data/word.txt");
      AggregateOperator<Tuple2<String, Integer>> hdfs = text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
          @Override
          public void flatMap(String line, Collector<Tuple2<String, Integer>> collector) throws Exception {
              String[] sp = line.split(" ");
              for (String s : sp) {
                  collector.collect(Tuple2.of(s, 1));
              }
          }
      }).groupBy(0).sum(1);
      hdfs.writeAsText("hdfs://n1:9000/tmp/8", FileSystem.WriteMode.OVERWRITE);
      env.execute();
  }
}
