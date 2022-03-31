package com.datapipeline.api;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.runtime.jobgraph.JobType;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;
import org.apache.flink.streaming.api.graph.GlobalStreamExchangeMode;
import org.apache.flink.streaming.api.graph.StreamGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BatchJobWriteFs {
    public static void main(String[] args) throws Exception {
        ParameterTool tool = ParameterTool.fromArgs(args);
        String output = tool.get("output");
        long numRecords = 70000;
        int parallelism = 1;
        int recordSize = 70000;
        long numRecordsToSend = 70000;
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(parallelism);
        DataStream<byte[]> source =
                env.addSource(new ByteArraySource(numRecordsToSend, recordSize, numRecords));
        DataStreamSink<byte[]> dataStreamSink = source.rebalance()
                .writeAsText(output, FileSystem.WriteMode.OVERWRITE);

        StreamGraph streamGraph = env.getStreamGraph();
        streamGraph.setGlobalStreamExchangeMode(GlobalStreamExchangeMode.ALL_EDGES_BLOCKING);
        streamGraph.setJobType(JobType.BATCH);

        env.execute(streamGraph);
    }

    private static class ByteArraySource implements ParallelSourceFunction<byte[]> {
        private final long numRecordsToSend;
        private final List<byte[]> records = new ArrayList<>();
        private volatile boolean isRunning = true;

        ByteArraySource(long numRecordsToSend, int recordSize, long numRecords) {
            this.numRecordsToSend = numRecordsToSend;
            Random random = new Random();
            for (int i = 0; i < numRecords; ++i) {
                byte[] record = new byte[recordSize];
                random.nextBytes(record);
                records.add(record);
            }
        }

        @Override
        public void run(SourceContext<byte[]> sourceContext) {
            int counter = 0;
            while (isRunning && counter++ < numRecordsToSend) {
                int temp = counter % records.size();
                sourceContext.collect(records.get(temp));
            }
        }

        @Override
        public void cancel() {
            isRunning = false;
        }
    }
}
