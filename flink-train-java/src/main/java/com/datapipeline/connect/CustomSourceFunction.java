package com.datapipeline.connect;

import com.datapipeline.utils.MysqlModel;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.types.Row;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomSourceFunction extends RichSourceFunction<Row> {

    Connection connection =null;

    Statement statement = null;

    private String username;
    private String password;
    private String url;
    private String schemaAndname;

    private MysqlModel model;



    public CustomSourceFunction(MysqlModel model) {
        this.model = model;
        this.username=model.getUsername();
        this.password=model.getPassword();
        this.url=model.getUrl();
        this.schemaAndname=model.getSchemaAndName();
        System.out.println("构造函数执行............");
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url,username,password);
        statement = connection.createStatement();
        System.out.println("open 方法执行.........");
    }

    @Override
    public void run(SourceContext<Row> ctx) throws Exception {
        Thread.sleep(1000*30);
        System.out.println("run 方法执行..........");
        ResultSet resultSet = statement.executeQuery("select count(0) as cnt from " + schemaAndname);
        Long cnt = 0L;
        while(resultSet.next()){
          cnt =  resultSet.getLong("cnt");
        }
        Row row = new Row(2);
        row.setField(0,schemaAndname);
        row.setField(1,cnt);
        ctx.collect(row);
    }

    /**
     * 这个 cancel方法貌似没什么用，如果程序正常结束是不是调用这个方法的，如果在页面上点击 cancel按钮，会调用这个方法
     */
    @Override
    public void cancel() {
        System.out.println("cancel方法执行.........");
        try {
            super.clone();
            if(connection!=null){
                connection.close();
            }
            if(statement!=null){
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
