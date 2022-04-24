package com.datapipeline.connect;


import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MysqlSinkFunction extends RichSinkFunction<String> {

    private static final String sql =
            "insert into"
            + " `pageView`(`windowEnd`,`rank1`,`pageView1`,`rank2`,`pageView2`,`rank3`,`pageView3`,`currentTime`)"
            + " values(?,?,?,?,?,?,?,?)";
    protected PreparedStatement pst;
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.8.128:3306/datapipeline", "root", "123456");
        pst = con.prepareStatement(sql);
    }

    @Override
    public void invoke(String value, Context context) throws Exception {
        String[] split = value.split("\n");
        String timeWindow = split[1].substring(split[1].indexOf("窗口结束时间："));
        String rank1 = split[2].substring(split[2].indexOf("NO 1: 页面URL = "), split[2].indexOf("浏览量") - 1);
        String pageView1 = split[2].substring(split[2].indexOf("浏览量 = "));
        String rank2 = split[3].substring(split[3].indexOf("NO 2: 页面URL = "), split[3].indexOf("浏览量") - 1);
        String pageView2 = split[3].substring(split[3].indexOf("浏览量 = "));
        String rank3 = split[4].substring(split[4].indexOf("NO 3: 页面URL = "), split[4].indexOf("浏览量") - 1);
        String pageView3 = split[4].substring(split[4].indexOf("浏览量 = "));
        String currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        pst.setString(1,timeWindow);
        pst.setString(2,rank1);
        pst.setString(3,pageView1);
        pst.setString(4,rank2);
        pst.setString(5,pageView2);
        pst.setString(6,rank3);
        pst.setString(7,pageView3);
        pst.setString(8,currentTime);
        pst.execute();
    }
}
