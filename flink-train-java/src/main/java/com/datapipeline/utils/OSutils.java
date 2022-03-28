package com.datapipeline.utils;


import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import java.text.DecimalFormat;

/**
 * @author ly
 * @Date Create in 16:15 2021/2/26 0026
 * @Description
 */

public class OSutils {

    public static void main(String[] args) {
        System.out.println(getCPU());
        System.out.println(getMemory());
    }

    public static String getMemory(){
            SystemInfo systemInfo = new SystemInfo();
            GlobalMemory memory = systemInfo.getHardware().getMemory();
            long totalByte = memory.getTotal();
            long acaliableByte = memory.getAvailable();

            String memorys = formatByte(totalByte)+","+new DecimalFormat("#.##%").format((totalByte-acaliableByte)*1.0/totalByte);

//            log.info("内存大小 = {},内存使用率 ={}",formatByte(totalByte),new DecimalFormat("#.##%").format((totalByte-acaliableByte)*1.0/totalByte));
            return  memorys;
    }
    public static String formatByte(long byteNumber){
        double FORMAT = 1024.0;
        double kbNumber = byteNumber/FORMAT;
        if(kbNumber<FORMAT){
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber/FORMAT;
        if(mbNumber<FORMAT){
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber/FORMAT;
        if(gbNumber<FORMAT){
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber/FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }


    public static String getCPU()  {
            SystemInfo systemInfo = new SystemInfo();
            CentralProcessor processor = systemInfo.getHardware().getProcessor();
            long[] prevTicks = processor.getSystemCpuLoadTicks();
            long[] ticks = processor.getSystemCpuLoadTicks();
            long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
            long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
            long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

            String cup = processor.getLogicalProcessorCount()+","+new DecimalFormat("#.##%").format(1.0-(idle * 1.0 / totalCpu));
//            log.info("CPU总数 = {},CPU利用率 ={}",processor.getLogicalProcessorCount(),new DecimalFormat("#.##%").format(1.0-(idle * 1.0 / totalCpu)));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
        }
            return  cup;
    }
}
