package com.datapipeline.operator.multiple;

// 主要看一下connect在生产中的实际应用案例

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.KafkaSourceBuilder;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.operators.StreamingRuntimeContext;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.connectors.kafka.config.OffsetCommitMode;
import org.apache.flink.streaming.connectors.kafka.internals.AbstractFetcher;
import org.apache.flink.streaming.connectors.kafka.internals.AbstractPartitionDiscoverer;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicsDescriptor;
import org.apache.flink.util.SerializedValue;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

/**
 * 在生产环境中，我们经常会遇到双流匹配的案例，例如：
 *
 * 一个订单包含了订单主体信息和商品的信息。
 * 外卖行业，一个订单包含了订单付款信息和派送信息。
 * 互联网广告行业，一次点击包含了用户的点击行为日志和计费日志。
 * 等其他相关的案例
 * 上述这些案例都需要涉及到双流匹配的操作，也就是所谓的双流 join。下面用一个案例来详解如何用 connect 实现双流 join。
 *
 * 本文案例
 * 一个订单分成了大订单和小订单，大小订单对应的数据流来自 Kafka 不同的 Topic，需要在两个数据流中按照订单 Id 进行匹配，
 * 这里认为相同订单 id 的两个流的延迟最大为 60s。
 * 大订单和小订单匹配成功后向下游发送，若 60s 还未匹配成功，意味着当前只有一个流来临，则认为订单异常，需要将数据进行侧流输出。
 *
 * 思路描述
 * 提取两个流的时间戳，因为要通过订单 Id 进行匹配，所以这里按照订单 Id 进行 keyBy，然后两个流 connect，大订单和小订单的处理逻辑一样，
 * 两个流通过 ValueState 进行关联。假如大订单流对应的数据先来了，需要将大订单的相关信息保存到大订单的 ValueState 状态中，
 * 注册一个 60s 之后的定时器。
 *
 * 如果 60s 内来了小订单流对应的数据来了，则将两个数据拼接发送到下游。
 * 如果 60s 内小订单流对应的数据还没来，就会触发 onTimer，然后进行侧流输出。
 * 如果小订单流对应的数据先到，也是同样的处理逻辑，先将小订单的信息保存到小订单的 ValueState 中，注册 60s 之后的定时器。
 */

public class ConnectOperation {
    public static void main(String[] args) {
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()  // 注意：这个位置必须这么写，不然后面的代码会报错
                .setBootstrapServers("")
                .setTopics("")
                .setGroupId("")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setBounded(OffsetsInitializer.timestamp(1592323200L))  //使用 batch mode模式读取kafka,当offset到达这个时间戳后，就会停止读取
//                .setUnbounded(OffsetsInitializer.offsets())   // 使用streaming mode模式读取kafka,但是到达一定条件后也能停止这个source
                .build();
        // fromSource 是新版本增加的api(应该是从1.13版本开始的）
        DataStreamSource<String> source = senv.fromSource(kafkaSource, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(60)), "");

    }
}
