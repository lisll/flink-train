package com.datapipeline.java8.java.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Instant 类  是Java8 中补充的一个 时间戳类。
 * 相较于 System.currentTimeMillis()获取到【毫秒】，Instant 可以更为精确的获取到【纳秒】。
 */
public class InstantDemo {

  /**
   * Instant 类的常用API 就是获取时间戳了
   *  * Instant 类的 getEpochSecond() : 获取的是秒
   *  * Instant 类的 toEpochMilli() : 获取的是毫秒，同 System.currentTimeMillis()
   *  * Instant 类的 getNano() : 获取的是纳秒，更精确了
   *  同时，Instant 类还是 Java8 中 提供的新的 日期时间类LocalDateTime 与 原来的 java.util.Date 类之间转换的桥梁。
   * @param args
   */
  public static void main(String[] args) {
     // 1,获取当前时间的Instant对象
    Instant now = Instant.now();
    System.out.println(now);
    System.out.println("纪元秒"+now.getEpochSecond());
    System.out.println("时间戳: "+System.currentTimeMillis());
    System.out.println("时间戳,同System.currentTimeMillis()"+now.toEpochMilli());
    System.out.println("纳秒 :"+now.getNano());
    // 2,提取指定时间的Instant对象
    Instant instant = Instant.ofEpochMilli(1680493719443L);
   //3.指定时间戳创建 带时区的日期时间对象 ZoneDateTime
    Instant instant02 = Instant.ofEpochSecond(1680493940L);
    ZonedDateTime zonedDateTime = instant02.atZone(ZoneId.of("Asia/Shanghai"));
    System.out.println("zonedDateTime->"+zonedDateTime);
    // 4.指定时间戳创建  默认时区的日期时间对象 LocalDateTime
    Instant instant03 = Instant.ofEpochSecond(1647784071); // 2022-03-20 21:47:51
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant03, ZoneId.systemDefault());
    System.out.println("localDateTime = " + localDateTime);
    System.out.println("===========================");
  }
}
