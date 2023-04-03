package com.datapipeline.java8.java.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.util.Set;
import java.util.stream.Collectors;

public class LocalDateTimeDemo {

  public static void main(String[] args) {
    parseString();
  }

  // 1,创建LocalDateTime 对象
  /**
   * * 1.创建日期时间对象 * 1.1 获取当前日期时间对象 * LocalTime.now() : 获取默认时区下的系统时钟的时间 * LocalTime.now(Clock clock)
   * : 获取指定时钟的时间 * LocalTime.now(ZoneId zone) : 获取指定时区的、默认系统时钟的时间 * 【补充：获取所有时区信息的方式 ：
   * ZoneId.getAvailableZoneIds()】 Asia/Shanghai * 1.2 获取指定日期时间对象 * LocalDateTime.of(int year, Month
   * month, int dayOfMonth, int hour, int minute) : 指定 年、月（枚举值）、日、小时、分钟 * LocalDateTime.of(int year,
   * Month month, int dayOfMonth, int hour, int minute, int second) : 指定 年、月（枚举值）、日、小时、分钟、秒 *
   * LocalDateTime.of(int year, Month month, int dayOfMonth, int hour, int minute, int second, int
   * nanoOfSecond) : 指定 年、月（枚举值）、日、小时、分钟、秒、纳秒 * LocalDateTime.of(int year, int month, int
   * dayOfMonth, int hour, int minute) : 指定 年、月（数值）、日、小时、分钟 * LocalDateTime.of(int year, int month,
   * int dayOfMonth, int hour, int minute, int second) : 指定 年、月（数值）、日、小时、分钟、秒 * LocalDateTime.of(int
   * year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond) : 指定
   * 年、月（数值）、日、小时、分钟、秒、纳秒 * LocalDateTime.of(LocalDate date, LocalTime time) : 指定 日期对象 和 时间对象 两个对象 *
   * LocalDateTime ofInstant(Instant instant, ZoneId zone) : 指定时间戳对象 Instant 和 时区 * LocalDateTime
   * ofEpochSecond(long epochSecond, int nanoOfSecond, ZoneOffset offset) : 指定 距离 1970-01-01
   * 00:00:00 的秒数、纳秒数、偏移数，创建LocalDateTime对象 *
   */
  public static void getLocalDateTime() {
    System.out.println(LocalDateTime.now());
    System.out.println(LocalDateTime.now(Clock.systemUTC()));
    System.out.println(LocalDateTime.now(ZoneId.systemDefault()));
    System.out.println(LocalDateTime.of(2023, Month.JANUARY, 3, 5, 6));
    System.out.println(LocalDateTime.of(2023, Month.JANUARY, 7, 6, 8, 45));
    System.out.println(LocalDateTime.of(2023, Month.FEBRUARY, 4, 13, 45, 34, 9899));
    System.out.println(LocalDateTime.of(2023, 5, 23, 18, 45));
    System.out.println(LocalDateTime.of(2023, 4, 02, 21, 46, 45, 6790));
    System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
    System.out.println(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
    // ZoneOffset zoneOffset09 = ZoneOffset.of("+8"); // +8小时
    // ZoneOffset zoneOffset09 = ZoneOffset.UTC; // 标准时区 零时区的时间
    // ZoneOffset zoneOffset09 = ZoneOffset.MAX; // +18 最大偏移
    // ZoneOffset zoneOffset09 = ZoneOffset.MIN; // -18 最小偏移
    // ZoneOffset zoneOffset09 = ZoneOffset.ofHours(2); // 增加两个小时
    System.out.println(LocalDateTime.ofEpochSecond(100, 453225, ZoneOffset.ofHours(1)));
    Set<String> asia =
        ZoneId.getAvailableZoneIds().stream()
            .filter(s -> s.startsWith("Asia"))
            .collect(Collectors.toSet());
    System.out.println(asia);
  }

  //  2 ,获取LocalDateTime对象的属性信息
  /**
   * * 2.获取日期时间对象的信息 * toLocalDate() : 返回一个LocalDate 对象 * toLocalTime() : 返回一个LocalTime 对象 * *
   * getYear() : 获取年分信息 * getMonth() : 获取月份信息（枚举类型） * getMonthValue() : 获取月份的数字（数值类型） *
   * getDayOfMonth() : 获取日期信息 * getDayOfWeek() : 获取星期几 （枚举类型） * getDayOfYear() : 获取这一年的第几天 * *
   * getHour() : 获取小时信息 * getMinute() : 获取分钟信息 * getSecond() : 获取秒 * getNano() : 获取纳秒 *
   */
  public static void attributeOfLocalDateTime() {
    LocalDateTime nowDateTime = LocalDateTime.now(ZoneId.systemDefault());
    System.out.println(nowDateTime.toLocalDate());
    System.out.println(nowDateTime.toLocalTime());
    System.out.println(nowDateTime.getYear());
    System.out.println(nowDateTime.getMonth());
    System.out.println(nowDateTime.getMonthValue());
    System.out.println(nowDateTime.getDayOfMonth());
    System.out.println(nowDateTime.getDayOfWeek());
    System.out.println(nowDateTime.getDayOfYear());
    System.out.println(nowDateTime.getHour());
    System.out.println(nowDateTime.getMinute());
    System.out.println(nowDateTime.getSecond());
    System.out.println(nowDateTime.getNano());
  }
  // 3, 指定LocalDateTime对象的属性信息
  /**
   * * 3.指定日期时间对象的 年、月、日、时、分、秒、纳秒 * withYear(int) : 指定年分 * withMonth(int) : 指定月份 *
   * withDayOfMonth(int) : 指定日期 * withDayOfYear(int) : 指定一年中的多少天 * withHour(int) : 指定小时 *
   * withMinute(int) : 指定分钟 * withSecond(int) : 指定秒 * withNano(int) : 指定纳秒 * *
   * with(TemporalAdjuster) : 时间矫正器 *
   */
  public static void modifyLocalDateTime() {
    LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
    System.out.println(now);
    System.out.println(now.withYear(2024));
    System.out.println(now.withMonth(5));
    System.out.println(now.withDayOfMonth(23));
    System.out.println(now.withDayOfYear(45));
    System.out.println(now.withHour(15));
    System.out.println(now.withMinute(45));
    System.out.println(now.withMinute(34));

    TemporalAdjuster temporalAdjuster =
        temporal -> {
          LocalDateTime localDateTime = (LocalDateTime) temporal;
          localDateTime = localDateTime.withYear(2024).withMonth(6).withDayOfMonth(3).withHour(14);
          return localDateTime;
        };
    System.out.println(now.with(temporalAdjuster));
  }

  // 4, 增加/减去 年月日时分秒信息
  /**
   * * 4.加上 或者 减去 时、分、秒、纳秒 * plusYears(long) : 加几年 * plusMonths(long) : 加几个月 * plusDays(long) : 加几天
   * * plusWeeks(long) : 加几个周 * plusHours(long) : 加几个小时 * plusMinutes(long) : 加几分钟 *
   * plusSeconds(long) : 加几秒 * plusNanos(long) : 加几个纳秒 * * minusYears(long) : 减几年 *
   * minusMonths(long) : 减几个月 * minusDays(long) : 减几天 * minusWeeks(long) : 减几个周 * minusHours(long) :
   * 减几个小时 * minusMinutes(long) : 减几分钟 * minusSeconds(long) : 减几秒 * minusNanos(long) : 减几个纳秒 *
   */
  public static void calcuLocalDateTime() {
    LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
    System.out.println("nowDate->" + now);
    System.out.println(now.plusYears(2));
    System.out.println(now.plusMonths(4));
    System.out.println(now.plusDays(8));
    System.out.println(now.plusWeeks(1));
    System.out.println(now.plusHours(4));
    System.out.println(now.plusMinutes(5));
    System.out.println(now.plusMinutes(5));
    System.out.println(now.plusNanos(5));
    System.out.println(now.minusYears(1));
    System.out.println(now.minusMonths(1));
    System.out.println(now.minusHours(5));
  }

  // 5, 比较两个日期时间的大小
  /**
   * * 5.比较两个日期时间对象的大小 * dateTime01.isEqual(dateTime02) : 两个日期时间对象是否相等 *
   * dateTime01.isAfter(dateTime02) : dateTime01 是否 比 dateTime02 晚 *
   * dateTiime01.isBefore(dateTime02) : dateTime01 是否 比 dateTime02 早 *
   */
  public static void equal() {
    LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
    LocalDateTime of = LocalDateTime.now();
    System.out.println(now);
    System.out.println(of);
    //    LocalDateTime of = LocalDateTime.of(2023, 4, 3, 10, 47, 56);
    System.out.println(now.isEqual(of));
    System.out.println(now.isBefore(of));
    System.out.println(now.isAfter(of));
  }

  // 6,LocalDateTime 对象 格式化为 字符串
  /**
   *  * 6. 日期时间对象 与 字符串 的相互转化
   *  *   6.1 日期时间对象 转 字符串 format() + DateTimeFormatter类
   *  *       String format01 = localDateTime.format(DateTimeFormatter.ISO_DATE_TIME); //2022-03-23T22:11:36.946
   *  *       String format02 = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);//2022-03-23T22:11:36.946
   *  *       String format03 = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //2022-03-23 22:11:36
   *  *   6.2 字符串 转 日期时间对象 parse()
   *  *       LocalDateTime localDateTimeParse = LocalDateTime.parse("2022--03--23 22::07::58", DateTimeFormatter.ofPattern("yyyy--MM--dd HH::mm::ss"));
   *  *
   */
  public static void parseString(){
    LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
    System.out.println(now.format(DateTimeFormatter.ISO_DATE_TIME));
    System.out.println(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    System.out.println(now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
    System.out.println(LocalDateTime.parse("2022--03--23 22--03--34",DateTimeFormatter.ofPattern("yyyy--MM--dd HH--mm--ss")));
  }

}
