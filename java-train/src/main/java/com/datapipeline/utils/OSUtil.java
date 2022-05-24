package com.datapipeline.utils;

import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class OSUtil {
  private static final int CPUTIME = 30;

  private static final int PERCENT = 100;

  private static final int FAULTLENGTH = 10;

  private static final File versionFile = new File("/proc/version");

  private static String linuxVersion = null;

    public static void main(String[] args) {
      AtomicInteger flag = new AtomicInteger();
      System.out.println(getMonitorInfo());
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      String format = simpleDateFormat.format(new Date());
      System.out.println(format);
    }

  public static MonitorInfoModel getMonitorInfo() {
    int kb = 1024*1024;
    // 可使用内存
    long totalMemory = Runtime.getRuntime().totalMemory() / kb;
    // 剩余内存
    long freeMemory = Runtime.getRuntime().freeMemory() / kb;
    // 最大可使用内存
    long maxMemory = Runtime.getRuntime().maxMemory() / kb;
    // 操作系统
    String osName = System.getProperty("os.name");
    OperatingSystemMXBean osmxb =
        (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    // 总的物理内存
    long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;
    // 剩余的物理内存
    long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;
    // 已使用的物理内存
    long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / kb;
    // 获得线程总数
    ThreadGroup parentThread;
    for (parentThread = Thread.currentThread().getThreadGroup();
        parentThread.getParent() != null;
        parentThread = parentThread.getParent())
      ;

    int totalThread = parentThread.activeCount();

    double cpuRatio = 0;

//    if (osName.toLowerCase().startsWith("windows")) {
//
//      cpuRatio = this.getCpuRatioForWindows();
//
//    } else {
//      cpuRatio = this.getCpuRateForLinux();
//    }
      MonitorInfoModel model = new MonitorInfoModel();
      model.setTotalMemory(totalMemory);
      model.setFreeMemory(freeMemory);
      model.setMaxMemory(maxMemory);
      model.setTotalMemorySize(totalMemorySize);
      model.setFreePhysicalMemorySize(freePhysicalMemorySize);
      model.setUsedMemory(usedMemory);
      model.setTotalThread(totalThread);
      return model;
  }
}
