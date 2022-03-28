package com.datapipeline.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author ly
 * @Date Create in 17:33 2021/2/26 0026
 * @Description
 */
public class FileUtil implements Runnable{

    private String localPath;
    public FileUtil(String localPath){
        this.localPath=localPath;
    }

//    String localPath = "/tmp/computer.log";

    public static void main(String[] args) {
       Thread thread = new Thread(new FileUtil(args[0]));
       thread.start();
    }

    public static String getComputer(){
        Properties props=System.getProperties(); //获得系统属性集
        String osName = props.getProperty("os.name"); //操作系统名称
        String osArch = props.getProperty("os.arch"); //操作系统构架
        String osVersion = props.getProperty("os.version"); //操作系统版本
        String cpu = OSutils.getCPU();
        String[] split_cpu = cpu.split(",");
        String total_cup=split_cpu[0];
        String cup_ratio=split_cpu[1];
        String memory = OSutils.getMemory();
        String[] split_memory = memory.split(",");
        String total_memory = split_memory[0];
        String memory_ratio = split_memory[1];
        String java_version = props.getProperty("java.version");
        String java_vm_vendo = props.getProperty("java.vm.vendor");

        StringBuilder sb = new StringBuilder();
        sb.append(osName).append("|")
          .append(osArch).append("|")
          .append(osVersion).append("|")
          .append(total_cup).append("|")
          .append(cup_ratio).append("|")
          .append(total_memory).append("|")
          .append(memory_ratio).append("|")
          .append(java_version).append("|")
          .append(java_vm_vendo).append("|")
          .append(System.currentTimeMillis()).append("|")
          .append(DateFormat.getDateTimeInstance().format(new Date()))
          ;
        return sb.toString();
    }

    public static void writeLocalFile(String path,String line){
        FileWriter writer = null;
        try {
            writer = new FileWriter(path,true);
            BufferedWriter bf = new BufferedWriter(writer);
            bf.write(line);
            bf.newLine();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true){
            writeLocalFile(localPath,getComputer());
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
