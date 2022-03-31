package com.datapipeline.api;


import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class BatchJobWrite {
    public static void main(String[] args) throws Exception {
        ParameterTool tool = ParameterTool.fromArgs(args);
        String output = tool.get("output");
        Configuration configuration =  new Configuration();
        configuration.setString("execution.buffer-timeout","0");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        env.setRuntimeMode(RuntimeExecutionMode.BATCH);
        env.setParallelism(1);
        DataStreamSource<String> source = env.fromElements("flink", "kafka", "kafka");
        source.writeAsText(output, FileSystem.WriteMode.OVERWRITE);
        env.execute();
    }
}
