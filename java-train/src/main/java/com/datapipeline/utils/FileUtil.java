package com.datapipeline.utils;

import scala.tools.nsc.Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    public static void main(String[] args) throws InterruptedException {
        OSUtil osUtil = new OSUtil();
        while(true){
            Thread.sleep(1000*20);
            writeLocalFile("/tmp/OSUtil.txt",osUtil.getMonitorInfo().toString());

        }
    }

    public static void writeLocalFile(String path,String line){
        FileWriter writer = null;
        try {
            writer = new FileWriter(path,true);
            BufferedWriter bf = new BufferedWriter(writer);
            bf.write(line);
            bf.write(" "+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
            bf.newLine();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
