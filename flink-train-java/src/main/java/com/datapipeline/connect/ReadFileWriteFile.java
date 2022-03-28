package com.datapipeline.connect;

import com.datapipeline.model.Computer;
import com.datapipeline.utils.FileUtil;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.io.TextInputFormat;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.FileProcessingMode;
import org.apache.flink.util.Collector;

public class ReadFileWriteFile {
  public static void main(String[] args) throws Exception {

      ParameterTool parameterTool = ParameterTool.fromArgs(args);
      String input = parameterTool.get("input");
      String output = parameterTool.get("output");

      Thread thread = new Thread(new FileUtil(input));
      thread.start();

      StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
      senv.setParallelism(1);
//        DataStreamSource<String> fileSource = senv.readTextFile(localPath);
      // 读取不断变化的日志目录
      DataStreamSource<String> fileSource = senv.readFile(new TextInputFormat(new Path()), input, FileProcessingMode.PROCESS_CONTINUOUSLY, 12*1000, Types.STRING);
      SingleOutputStreamOperator<Computer> operator = fileSource.flatMap(new RichFlatMapFunction<String, Computer>() {
          @Override
          public void flatMap(String value, Collector<Computer> collector) throws Exception {
              String[] split_computer = value.split("\\|");
              //构建Computer对象
              if(split_computer.length>10){
                  String os_name = split_computer[0];
                  String os_arch = split_computer[1];
                  String os_version = split_computer[2];
                  String total_cup = split_computer[3];
                  String cup_ratio = split_computer[4];
                  String total_memory = split_computer[5];
                  String memory_ratio = split_computer[6];
                  String java_version = split_computer[7];
                  String java_vm_vendo = split_computer[8];
                  String eventTime = split_computer[9];
                  String reservedTime = split_computer[10];

                  Computer computer = new Computer.Builder(os_name, Integer.parseInt(total_cup), cup_ratio, total_memory, memory_ratio)
                          .setEventTime(Long.parseLong(eventTime))
                          .setReservedTime(reservedTime)
                          .build();
                  collector.collect(computer);
              }
          }
      });
//        operator.print();

      operator.global().writeAsText(output, FileSystem.WriteMode.OVERWRITE);
      senv.execute();
  }


}
