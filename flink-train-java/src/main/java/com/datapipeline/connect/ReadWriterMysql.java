package com.datapipeline.connect;

import com.datapipeline.utils.MysqlModel;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;

public class ReadWriterMysql {
    public static void main(String[] args) throws Exception {

//        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        senv.setParallelism(1);
        MysqlModel sourceModel = new MysqlModel("com.mysql.cj.jdbc.Driver","dp_test","123456","jdbc:mysql://82.157.66.105:3306/dp_test","`dp_test`.`a_flink_test_2`");
        MysqlModel sinkModel = new MysqlModel("com.mysql.cj.jdbc.Driver","root","Datapipeline123","jdbc:mysql://42.193.122.151:3306/dp-thrall","`dp-thrall`.`a_flink_test_2`");
        DataStreamSource<Row> source = senv.addSource(new CustomSourceFunction(sourceModel));
        DataStreamSource<Row> sink = senv.addSource(new CustomSourceFunction(sinkModel));
        source.union(sink).print();
        senv.execute("test");
    }
}
