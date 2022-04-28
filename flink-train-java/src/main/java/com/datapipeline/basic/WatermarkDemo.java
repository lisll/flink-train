package com.datapipeline.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.datapipeline.utils.DateUtils;
import com.google.gson.JsonObject;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssignerSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * 主要演示根据事件事件生成水印
 *
 *  {"userID": "user_5", "eventTime": "2019-08-18 09:17:20", "eventType": "click", "productID": 3}
 *  {"userID": "user_5", "eventTime": "2019-08-18 09:17:21", "eventType": "browse", "productID": 3}
 *  {"userID": "user_5", "eventTime": "2019-08-18 09:17:22", "eventType": "browse", "productID": 3}
 *  {"userID": "user_5", "eventTime": "2019-08-18 09:17:29", "eventType": "click", "productID": 3}
 *  {"userID": "user_5", "eventTime": "2019-08-18 09:17:39", "eventType": "browse", "productID": 3}
 *  {"userID": "user_5", "eventTime": "2019-08-18 09:17:49", "eventType": "browse", "productID": 3}
 */
public class WatermarkDemo {
    public static void main(String[] args) {
     final  StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
     senv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        ParameterTool tool = ParameterTool.fromArgs(args);
        String hostname = tool.get("hostname");
        String port = tool.get("port");
        // 生成一个周期性的水印
        DataStream<JSONObject> socketSource = senv.socketTextStream(hostname, Integer.parseInt(port)).map(JSONObject::parseObject);
        socketSource.assignTimestampsAndWatermarks(WatermarkStrategy
                        .<JSONObject>forBoundedOutOfOrderness(Duration.ofSeconds(5))//水印策略
                        .withTimestampAssigner((event, ts) -> {
                            String format = new SimpleDateFormat("yyyy-HH-dd hh:mm:ss").format(event.get("eventTime"));
                            return Long.parseLong(format);
                        }));

    }
}
