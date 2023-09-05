package com.datapipeline.state;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.scala.typeutils.Types;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class OperatorListStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(3);
        env.socketTextStream("127.0.0.1", 7777).map(new MyCountMapFunction()).print();
        env.execute();
    }

      // 1,实现 CheckpointedFunction 接口
    public static class MyCountMapFunction implements MapFunction<String, Long>, CheckpointedFunction {
        private Long count = 0L;
        private ListState<Long> state;

        @Override
        public Long map(String value) throws Exception {
            return ++count;
        }

        /**
         * 2.本地变量持久化：将本地变量拷贝到算子状态中,开启checkpoint时才会调用
         */
        @Override
        public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
            System.out.println("snapshotState...");
            // 清空算子状态
            state.clear();
            // 将本地变量加入到算子状态中
            state.add(count);
        }

        // 3.初始化本地变量：程序启动和恢复时， 从状态中 把数据添加到 本地变量，每个子任务调用一次
        @Override
        public void initializeState(FunctionInitializationContext context) throws Exception {
            System.out.println("initializeState...");
            //3.1 从上下文初始化算子状态
            state = context.getOperatorStateStore()
                    .getListState(new ListStateDescriptor<>("state", Types.LONG()));
           //3.2 从算子状态中把数据拷贝到本地变量
            if(context.isRestored()){
                for (Long aLong : state.get()) {
                    count+=aLong;
                }
            }
            System.out.println("initializeState--->"+count);
        }
    }
}
