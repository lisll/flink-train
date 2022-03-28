package com.datapipeline.model;

import java.io.Serializable;

/**
 * @author ly
 * @Date Create in 16:35 2021/2/26 0026
 * @Description
 */
public class Computer implements Serializable {
    public long timeStamp = System.currentTimeMillis();   // 获取当前系统的毫秒值
    public String os_name; //名称
    public String os_arch; //架构
    public double os_version; //版本
    public int total_cup; //cup总数
    public String cup_ratio; //cup利用率
    public String total_memory; //内存总数
    public String memory_ratio; //内存利用率
    public String java_version; //java版本
    public String java_vm_vendo; //java虚拟机实现供应商
    public long eventTime ; //事件时间
    public String reservedTime ;  //保留值


    private Computer(Builder builder){
            this.os_name=builder.os_name;
            this.os_arch=builder.os_arch;
            this.os_version=builder.os_version;
            this.total_cup=builder.total_cup;
            this.cup_ratio=builder.cup_ratio;
            this.total_memory=builder.total_memory;
            this.memory_ratio=builder.memory_ratio;
            this.java_version=builder.java_version;
            this.java_vm_vendo=builder.java_vm_vendo;
            this.eventTime=builder.eventTime;
            this.reservedTime=builder.reservedTime;

    }

    public static class Builder{
        public String os_name; //必选
        public int total_cup; //必选
        public String cup_ratio; //必选
        public String total_memory; //必选
        public String memory_ratio; //必选
        public String java_version; //可选
        public String java_vm_vendo; //可选
        public String os_arch; //可选
        public double os_version; //可选

        public long eventTime ; //事件时间
        public String reservedTime ;  //保留值

        public Builder(String os_name,int total_cup,String cup_ratio,String total_memory,String memory_ratio){
            this.os_name=os_name;
            this.total_cup=total_cup;
            this.cup_ratio=cup_ratio;
            this.total_memory=total_memory;
            this.memory_ratio=memory_ratio;

        }

        public Builder setJava_version(String java_version){
            this.java_version=java_version;
            return this;
        }

        public Builder setJava_vm_vendo(String java_vm_vendo){
            this.java_vm_vendo=java_vm_vendo;
            return this;
        }
        public Builder setOs_arch(String os_arch){
            this.os_arch=os_arch;
            return this;
        }
        public Builder setOs_version(double os_version){
            this.os_version=os_version;
            return this;
        }

        public Builder setEventTime(long eventTime ){
            this.eventTime=eventTime;
            return this;
        }

        public Builder setReservedTime(String reservedTime ){
            this.reservedTime=reservedTime;
            return this;
        }


        public Computer build(){
            return new Computer(this);
        }

    }

    @Override
    public String toString() {
        return "Computer{" +
                "timeStamp=" + timeStamp +
                ", os_name='" + os_name + '\'' +
                ", os_arch='" + os_arch + '\'' +
                ", os_version=" + os_version +
                ", total_cup=" + total_cup +
                ", cup_ratio='" + cup_ratio + '\'' +
                ", total_memory='" + total_memory + '\'' +
                ", memory_ratio='" + memory_ratio + '\'' +
                ", java_version='" + java_version + '\'' +
                ", java_vm_vendo='" + java_vm_vendo + '\'' +
                ", eventTime=" + eventTime +
                ", reservedTime='" + reservedTime + '\'' +
                '}';
    }
}
