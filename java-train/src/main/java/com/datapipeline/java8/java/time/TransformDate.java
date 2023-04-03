package com.datapipeline.java8.java.time;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Java8中提供了新的日期时间类，帮助我们更便捷的操作日期和时间对象。
 * 针对原来的Date类，如何在新的日期时间对象与Date类之间相互转化，本文做出详细的总结。
 *
 * <p>Java8中的Date与各类之间的关系 各个类的功能说明如下 ：
 * LocalDate : Java8 日期类(默认时区，中国就是东八区)
 * LocalTime : Java8 时间类(默认时区，中国就是东八区)
 * LocalDateTime : Java8 日期时间类(默认时区，中国就是东八区)
 * Instant : Java8 时间戳类
 * ZoneDateTime :Java8 带时区的日期时间类
 * Date : Java中的Date类，包 java.util.Date
 */
public class TransformDate {

  public static void main(String[] args) {
    dateTransLocalDateTime();
  }

  // 1, LocalDate转Date
  public static void localDateTransDate() {
    // 0.准备一个Date的格式化对象
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 1.LocalDate 转 Date
     * 思路1 ： LocalDate --> LocalDateTime --> Instant --> Date
     * 思路2 ： LocalDate --> LocalDateTime --> ZoneDateTime --> Instant --> Date
     */
    LocalDate localDate = LocalDate.of(2023,4,3);
    System.out.println("localDate-> "+localDate);
    LocalDateTime localDateTime = localDate.atStartOfDay();
    System.out.println("localDateTime-> "+localDateTime);
    Instant instant = localDateTime.toInstant(ZoneOffset.of("-1"));
    System.out.println("instant-> "+instant);
//    Instant instant = localDateTime.toInstant(ZoneOffset.ofHours(21));
    java.util.Date from = Date.from(instant);
    System.out.println(sdf.format(from));

    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
    System.out.println("zonedDateTime-> "+zonedDateTime);
    Instant instant1 = zonedDateTime.toInstant();
    System.out.println("instant1->"+instant1);
    System.out.println(sdf.format(Date.from(instant1)));
  }
  // 2, LocalDateTime转Date
  public static void localDateTimeTransDate() {
    //0.准备一个Date的格式化对象
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 2.LocalDateTime 转 Date
     * 思路1 ： LocalDateTime --> Instant --> Date
     * 思路2 ： LocalDateTime --> ZoneDateTime --> Instant --> Date
     */
    LocalDateTime localDateTime = LocalDateTime.of(2023, 3, 25, 22, 34, 30);
    System.out.println("localDateTime-> "+localDateTime);
    Instant instant = localDateTime.toInstant(ZoneOffset.of("+8"));
    System.out.println("instant -> "+instant);
    System.out.println(sdf.format(Date.from(instant)));

    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
    System.out.println("zonedDateTime-> "+zonedDateTime);
    Instant instant1 = zonedDateTime.toInstant();
    System.out.println("instant1-> "+instant1);
    System.out.println(sdf.format(Date.from(instant1)));
  }

  // 3, ZoneDateTime转Date
  public static void zoneDateTimeTransDate() {
    //0.准备一个Date的格式化对象
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 3.ZoneDateTime 转 Date
     * 思路 ： ZoneDateTime --> Instant --> Date
     */

    ZonedDateTime zonedDateTime = ZonedDateTime.of(2022, 3, 24, 13, 45, 24, 9, ZoneId.systemDefault());
    System.out.println("zonedDateTime-> "+zonedDateTime);
    Instant instant = zonedDateTime.toInstant();
    System.out.println("instant-> "+instant);
    System.out.println(sdf.format(Date.from(instant)));
  }

  // 4, Date转ZoneDateTime
  public static void dateTransZoneDateTime() {
    //0.准备一个Date的格式化对象
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 4.Date 转 ZoneDateTime
     * 思路 ： Date --> Instant --> ZoneDateTime
     */
    java.util.Date date = new java.util.Date();
    Instant instant = date.toInstant();
    System.out.println("date-> "+sdf.format(date));
    System.out.println("instant-> "+instant);
    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
    System.out.println("zonedDateTime-> "+zonedDateTime);
  }

  // 5, Date转LocalDateTime
  public static void dateTransLocalDateTime() {
    //0.准备一个Date的格式化对象
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 5.Date 转 LocalDateTime
     * 思路1 ： Date --> Instant --> ZoneDateTime --> LocalDateTime
     * 思路2 ： Date --> Instant --> LocalDateTime.ofInstant(Instant,ZoneId)
     */
    java.util.Date date = new java.util.Date();
    System.out.println("date-> "+sdf.format(date));
    Instant instant = date.toInstant();
    System.out.println("instant-> "+instant);
    LocalDateTime localDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        .toLocalDateTime();
    System.out.println("localDateTime-> "+localDateTime);
    LocalDateTime localDateTime1 = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    System.out.println("localDateTime1-> "+localDateTime1);
  }

  // 6, Date转LocalDate
  public static void dateTransLocalDate() {
//0.准备一个Date的格式化对象
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 6.Date 转 LocalDate
     * 思路1 ： Date --> Instant --> ZoneDateTime --> LocalDateTime --> LocalDate
     * 思路2 ： Date --> Instant --> LocalDateTime.of(Instant,ZoneId) --> LocalDate
     */
    java.util.Date date06 = new java.util.Date();
    Instant instant06 = date06.toInstant();
    //思路一 ： Date --> Instant --> ZoneDateTime --> LocalDateTime --> LocalDate
    LocalDate localDate0601 = ZonedDateTime.ofInstant(instant06, ZoneId.systemDefault()).toLocalDateTime().toLocalDate();
    //思路二 ： Date --> Instant --> LocalDateTime.of(Instant,ZoneId) --> LocalDate
    LocalDate localDate0602 = LocalDateTime.ofInstant(instant06, ZoneId.systemDefault()).toLocalDate();

    System.out.println("date06 = " + sdf.format(date06));
    System.out.println("instant06 = " + instant06);
    System.out.println("localDate0601 = " + localDate0601);
    System.out.println("localDate0602 = " + localDate0602);
    System.out.println("=======================");
  }
}
