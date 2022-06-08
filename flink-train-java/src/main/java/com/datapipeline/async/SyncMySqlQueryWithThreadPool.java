package com.datapipeline.async;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 这个异步IO读取MySQL的作用是啥？？？？
 */

public class SyncMySqlQueryWithThreadPool {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//    DataStreamSource<String> line = env.socketTextStream("localhost", 8888);
        DataStreamSource<String> line = env.fromElements("2");

        AsyncDataStream.orderedWait(line,new MySqlAsyncFunction(5),30000, TimeUnit.MILLISECONDS,20).print();

        env.execute();
    }

    static class MySqlAsyncFunction extends RichAsyncFunction<String, Tuple2<String, String>> {

        // 不能加transient
        private int maxConnTotal;
        private transient ExecutorService executorService;
        private DruidDataSource dataSource;

        public MySqlAsyncFunction(int maxConnTotal) {
            this.maxConnTotal = maxConnTotal;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            // 创建一个线程池，实现并发请求
            executorService = Executors.newFixedThreadPool(maxConnTotal);
            // 创建连接池（异步IO 一个请求就是一个线程，一个请求对应一个连接）
            dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUsername("root");
            dataSource.setPassword("Datapipeline123");
            dataSource.setUrl("jdbc:mysql://42.193.122.151:3306/dp-thrall?characterEncoding=UTF-8");
            dataSource.setMaxActive(maxConnTotal);
        }

        @Override
        public void close() throws Exception {
            executorService.shutdown();
            dataSource.close();
        }

        @Override
        public void asyncInvoke(String input, ResultFuture<Tuple2<String, String>> resultFuture)
                throws Exception {
            // 将查询请求放入线程池
            Future<String> future =
                    executorService.submit(
                            new Callable<String>() {
                                @Override
                                public String call() throws Exception {
                                    return queryFromMySql(input);
                                }
                            });

            CompletableFuture.supplyAsync(
                            new Supplier<String>() {
                                @Override
                                public String get() {
                                    try {
                                        return future.get();
                                    } catch (Exception e) {
                                        return null;
                                    }
                                }
                            })
                    .thenAccept(
                            (result) -> {
                                System.out.println("当前线程："+Thread.currentThread().getName()+",当前结果"+result);
                                resultFuture.complete(Collections.singleton(Tuple2.of(input, result)));
                            });
        }

        private String queryFromMySql(String param) throws SQLException {
            String sql = "SELECT validator_id, id FROM data_validator_diff_record WHERE validator_id = ? ";
            String result = null;
            Connection connection = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                connection = dataSource.getConnection();
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, param);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    result = rs.getString("id");
                }
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
            return result;
        }
    }
}
