package com.datapipeline.state;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class CheckpointConfigDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // TODO 检查点配置
        // 1、启用检查点: 默认是barrier对齐的，周期为5s, 精准一次
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        // 2、指定检查点的存储位置
        checkpointConfig.setCheckpointStorage("hdfs://n1:8020/chk");
        checkpointConfig.setCheckpointStorage(new MemoryStateBackend());
        // 3、checkpoint的超时时间: 默认10分钟
        checkpointConfig.setCheckpointTimeout(60000);
        // 4、同时运行中的checkpoint的最大数量
        checkpointConfig.setMaxConcurrentCheckpoints(3);
        // 5、最小等待间隔: 上一轮checkpoint结束 到 下一轮checkpoint开始 之间的间隔，设置了>0,并发就会变成1
        checkpointConfig.setMinPauseBetweenCheckpoints(1000);
        // 6、取消作业时，checkpoint的数据 是否保留在外部系统
        // DELETE_ON_CANCELLATION:主动cancel时，删除存在外部系统的chk-xx目录 （如果是程序突然挂掉，不会删）
        // RETAIN_ON_CANCELLATION:主动cancel时，外部系统的chk-xx目录会保存下来
        checkpointConfig.setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        // 7、允许 checkpoint 连续失败的次数，默认0--》表示checkpoint一失败，job就挂掉
        checkpointConfig.setTolerableCheckpointFailureNumber(2);
        // TODO 开启 非对齐检查点（barrier非对齐）
        // 开启的要求： Checkpoint模式必须是精准一次，最大并发必须设为1
        checkpointConfig.enableUnalignedCheckpoints();
        // 开启非对齐检查点才生效： 默认0，表示一开始就直接用 非对齐的检查点
        // 如果大于0， 一开始用 对齐的检查点（barrier对齐）， 对齐的时间超过这个参数，自动切换成 非对齐检查点（barrier非对齐）
        checkpointConfig.setAlignedCheckpointTimeout(Duration.ofSeconds(3));

        env
                .socketTextStream("hadoop102", 7777)
                .flatMap(
                        (String value, Collector<Tuple2<String, Integer>> out) -> {
                            String[] words = value.split(" ");
                            for (String word : words) {
                                out.collect(Tuple2.of(word, 1));
                            }
                        }
                )
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(value -> value.f0)
                .sum(1)
                .print();

        env.execute();
    }
}
