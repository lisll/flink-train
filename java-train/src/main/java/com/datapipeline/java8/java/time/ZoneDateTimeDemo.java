package com.datapipeline.java8.java.time;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet.VAR;

/**
 * ZoneDateTime ： 是带时区的日期时间类。
 * 区别于 LocalDateTime 类，LocalDateTime 类是 默认时区的日期时间类，中国默认的 时区是 【东八区】。
 * 因此，当LocalDateTime 手动指定 时区后 就变成了ZoneDateTime。
 * 时区id 可以通过ZoneId类的API 直接获取。
 */
public class ZoneDateTimeDemo {


  public static void main(String[] args) {
    //1.获取所有的时区id
    Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
    for (String availableZoneId : availableZoneIds) {
      System.out.println("availableZoneId->"+availableZoneId);
    }
    // 2.1 获取当前的时间 LocalDateTime ,中国 东八区，比标准的时区要早8个小时
    LocalDateTime now = LocalDateTime.now();
    System.out.println("now->"+now);
    // 2.2 获取标准时区的时间 ZoneDateTime
    ZonedDateTime now1 = ZonedDateTime.now(Clock.systemUTC());
    System.out.println(now1);
    // 2.3 使用计算机的默认时区
    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    System.out.println("default zonedDateTime-> "+zonedDateTime);
    // 2.4 指定时区id
    ZonedDateTime zoneDateTimeNow03 = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
    System.out.println("zoneDateTimeNow03---> "+zoneDateTimeNow03);

    // zoneDateTime 对象 转 LocalDateTime 对象
    System.out.println("zoneDateTimeNow03.toLocalDate() = " + zoneDateTimeNow03.toLocalDate());
    System.out.println("zoneDateTimeNow03.toLocalTime() = " + zoneDateTimeNow03.toLocalTime());
    System.out.println("zoneDateTimeNow03.toLocalDateTime() = " + zoneDateTimeNow03.toLocalDateTime());
    System.out.println("zoneDateTimeNow03.toOffsetDateTime() = " + zoneDateTimeNow03.toOffsetDateTime());
    System.out.println("zoneDateTimeNow03.toInstant() = " + zoneDateTimeNow03.toInstant());

//1.获取所有的时区id
//    Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
//    System.out.println("availableZoneIds = " + availableZoneIds);
//    System.out.println("====================================");
//
//    // 2.1 获取当前的时间 LocalDateTime ,中国 东八区，比标准的时区要早8个小时
//    LocalDateTime localDateTimeNow = LocalDateTime.now();
//    System.out.println("localDateTimeNow = " + localDateTimeNow); // localDateTimeNow = 2022-03-24T21:34:58.865
//
//    // 2.2 获取标准时区的时间 ZoneDateTime
//    ZonedDateTime zoneDateTimeNow01 = ZonedDateTime.now(Clock.systemUTC());
//    System.out.println("zoneDateTimeNow01 = " + zoneDateTimeNow01); // zoneDateTimeNow = 2022-03-24T13:34:58.866Z
//
//    // 2.3 使用计算机的默认时区
//    ZonedDateTime zoneDateTimeNow02 = ZonedDateTime.now();
//    System.out.println("zoneDateTimeNow02 = " + zoneDateTimeNow02);
//
//    // 2.4 指定时区id
//    ZonedDateTime zoneDateTimeNow03 = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
//    System.out.println("zoneDateTimeNow03 = " + zoneDateTimeNow03);
//
//    System.out.println();
//
//    // zoneDateTime 对象 转 LocalDateTime 对象
//    System.out.println("zoneDateTimeNow03.toLocalDate() = " + zoneDateTimeNow03.toLocalDate());
//    System.out.println("zoneDateTimeNow03.toLocalTime() = " + zoneDateTimeNow03.toLocalTime());
//    System.out.println("zoneDateTimeNow03.toLocalDateTime() = " + zoneDateTimeNow03.toLocalDateTime());
//    System.out.println("zoneDateTimeNow03.toOffsetDateTime() = " + zoneDateTimeNow03.toOffsetDateTime());
//    System.out.println("zoneDateTimeNow03.toInstant() = " + zoneDateTimeNow03.toInstant());
//
//    System.out.println("====================================");

  }

}
