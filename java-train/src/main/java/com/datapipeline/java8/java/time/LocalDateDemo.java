package com.datapipeline.java8.java.time;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/** LocalDate 类 是 Java8中新增的日期类，采用了系统的默认时区。 可以方便的处理 日期对象 的 年、月、日 信息。 */
public class LocalDateDemo {

  public static void main(String[] args) {
    others();
  }

  // 1,获取LocalDate 对象
  /**
   *  * 1.创建日期对象
   *  *   1.1 获取当前日期对象
   *  *       LocalDate.now() : 返回默认时区下的、系统始终下的 当前日期
   *  *       LocalDate.now(Clock clock) : 返回指定时钟的 当前日期
   *  *       LocalDate.now(ZoneId zone) : 返回指定时区的 当前日期
   *  *       【补充：获取所有时区信息的方式 ： ZoneId.getAvailableZoneIds()】
   *  *   1.2 获取指定日期对象
   *  *       LocalDate.of(int year, Month month, int dayOfMonth) : 指定年、月份名称、日期
   *  *       LocalDate.of(int year, int month, int dayOfMonth) : 指定 年、月份数字、日期
   *  *       LocalDate.ofYearDay(int year, int dayOfYear) : 指定 年分、这一年的第几天
   *  *       LocalDate.ofEpochDay(long epochDay) : 指定 距离 1970-01-01 00:00:00 的天数
   *  *
   */
  public static void getLocalDate() {
    System.out.println(LocalDate.now());
    System.out.println(LocalDate.now(Clock.systemUTC()));
    System.out.println(LocalDate.now(ZoneId.systemDefault()));
    System.out.println(LocalDate.of(2023, 04, 02));
    System.out.println(LocalDate.of(2023, Month.JANUARY, 02));
    System.out.println(LocalDate.ofYearDay(2023, 1));
    System.out.println(LocalDate.ofEpochDay(365));
  }
  // 2,获取 LocalDate 对象的 年月日信息
  /**
   *  * 2.获取日期对象的 年、月、日 信息
   *  *      getYear() : 获取年分信息
   *  *      getMonth() : 获取月份信息（枚举类型）
   *  *      getMonthValue() : 获取月份的数字（数值类型）
   *  *      getDayOfMonth() : 获取日期信息
   *  *      getDayOfWeek() : 获取星期几 （枚举类型）
   *  *      getDayOfYear() : 获取这一年的第几天
   *  *
   */
  public static void getMessage() {
    LocalDate nowDate = LocalDate.now(ZoneId.systemDefault());
    System.out.println(nowDate.getYear());
    System.out.println(nowDate.getMonth());
    System.out.println(nowDate.getMonthValue());
    System.out.println(nowDate.getDayOfMonth());
    System.out.println(nowDate.getDayOfWeek());
    System.out.println(nowDate.getDayOfYear());
  }

  // 3, 修改日期对象
  /**
   *  *  3.指定 日期对象的属性
   *  *      withYear(int) : 设置年
   *  *      withMonth(int) : 设置月
   *  *      withDayOfMonth(int) : 设置日
   *  *      withDayOfYear(int) : 设置这一年的第几天
   *  *
   *  *      with(TemporalAdjuster) : 时间调整器，更方便的调整
   *  *
   */
  public static void modifyLocalDate() {
    LocalDate nowDate = LocalDate.now(ZoneId.systemDefault());
    System.out.println("nowDate->" + nowDate);
    System.out.println(nowDate.withYear(2024));
    System.out.println(nowDate.withMonth(3));
    System.out.println(nowDate.withDayOfMonth(5));
    System.out.println(nowDate.withDayOfYear(99));
    TemporalAdjuster temporalAdjuster =
        temporal -> {
          System.out.println("temporal->"+temporal);
          LocalDate localDate = (LocalDate) temporal;
          localDate = localDate.withMonth(localDate.getMonthValue() + 1).withDayOfMonth(1);
          System.out.println("now Date ->"+localDate);
          return localDate;
        };
    System.out.println(nowDate.with(temporalAdjuster));
    System.out.println(nowDate.with(TemporalAdjusters.firstDayOfNextMonth()));
    System.out.println(nowDate.with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.MONDAY)));// 调整到这个月的第一个星期一
  }

  //4, 增加/减少 LocalDate 对象的 年月日 信息
  /**
   *  *  4.加上 或者 减去 年、月、日、周
   *  *      plusYears(long) : 加几年
   *  *      plusMonths(long) : 加几个月
   *  *      plusDays(long) : 加几天
   *  *      plusWeeks(long) : 加几个星期
   *  *
   *  *      minusYears(long) : 减几年
   *  *      minusMonths(long) : 减几个月
   *  *      minusDays(long) : 减几天
   *  *      minusWeeks(long) : 减几个星期
   *  *
   */
  public static void calcuLocalDate(){
    LocalDate nowDate = LocalDate.now(ZoneId.systemDefault());
    System.out.println(nowDate.plusYears(2));
    System.out.println(nowDate.plusMonths(3));
    System.out.println(nowDate.plusDays(4));
    System.out.println(nowDate.plusWeeks(2));
  }

  //5,LocalDate 对象的大小比较
  /**
   *  *      date01.isEqual(date02) : 两个日期是否相等
   *  *      date01.isAfter(date02) : date01 是否 比 date02 晚
   *  *      date01.isBefore(date02) : date01 是否 比 date02 早
   *  *
   */
  public static void equal(){
    LocalDate nowDate = LocalDate.now(ZoneId.systemDefault());
    LocalDate otherDate = LocalDate.of(2023, 4, 3);
    System.out.println(nowDate.isEqual(otherDate));
    System.out.println(nowDate.isBefore(otherDate));
    System.out.println(nowDate.isAfter(otherDate));
  }
  // 6,LocalDate 的格式化为 字符串
  /**
   *  *  6.日期对象与字符串的转化
   *  *    6.1 日期对象转字符串 format() + DateTimeFormatter类
   *  *        String format01 = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);    // 最常用 2022-03-22
   *  *        String format02 = localDate.format(DateTimeFormatter.ISO_DATE);          // 2022-03-23
   *  *        String format03 = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);    // 20220323
   *  *        String format04 = localDate.format(DateTimeFormatter.ISO_ORDINAL_DATE); // 2022-082 年分-当前日期是这一年的第几天
   *  *        String format05 = localDate.format(DateTimeFormatter.ISO_WEEK_DATE);    // 2022-W12-3 年分-第几周-星期几
   *  *        String format06 = localDate.format(DateTimeFormatter.ofPattern("yyyy--$--MM--$--dd")); // 自定义格式化
   *  *
   *  *
   *  *    6.2 字符串转日期对象 parse()
   *  *        LocalDate dateParse01 = LocalDate.parse("2022--$--03--$--23",DateTimeFormatter.ofPattern("yyyy--$--MM--$--dd"));
   *  *
   */
  public static void parseString(){
    LocalDate nowDate = LocalDate.now();
    System.out.println(nowDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    System.out.println(nowDate.format(DateTimeFormatter.ISO_DATE));
    System.out.println(nowDate.format(DateTimeFormatter.BASIC_ISO_DATE));
    System.out.println(nowDate.format(DateTimeFormatter.ISO_ORDINAL_DATE));
    System.out.println(nowDate.format(DateTimeFormatter.ISO_WEEK_DATE));
    System.out.println(nowDate.format(DateTimeFormatter.ofPattern("YYYY--$--MM--$--dd")));

    System.out.println(LocalDate.parse("2022--03--23",DateTimeFormatter.ofPattern("yyyy--MM--dd")));
  }

  // 7 计算两个LocalDate 对象的日期差

  /**
   *  *  7.计算两个日期的差  Period
   *  *    Period between = Period.between(datePeriod01, datePeriod02); // 小一点的日期，大一点的日期，否则会出现负数
   *  *    7.1 获取相差的【年月日】整体的一个效果
   *  *        between.getYears()
   *  *        between.getMonths()
   *  *        between.getDays()
   *  *    7.2 分别获取相差的【年】【月】【日】的数据
   *  *        ChronoUnit.YEARS.between(datePeriod01, datePeriod02));
   *  *        ChronoUnit.MONTHS.between(datePeriod01, datePeriod02));
   *  *        ChronoUnit.DAYS.between(datePeriod01, datePeriod02));
   *  *
   */
  public static void difference(){
    LocalDate datePeriod01 = LocalDate.of(2022,5,27);
    LocalDate datePeriod02 = LocalDate.of(2022,6,29);
    System.out.println(Period.between(datePeriod01,datePeriod02).getYears());
    System.out.println(Period.between(datePeriod01,datePeriod02).getMonths());
    System.out.println(Period.between(datePeriod01,datePeriod02).getDays());
    System.out.println(ChronoUnit.YEARS.between(datePeriod01, datePeriod02));
    System.out.println(ChronoUnit.MONTHS.between(datePeriod01, datePeriod02));
    System.out.println(ChronoUnit.DAYS.between(datePeriod01, datePeriod02));
  }
  public static void others(){
    LocalDate now = LocalDate.of(2023,2,18);
    System.out.println(now.isLeapYear());
    System.out.println(now.atStartOfDay());
    System.out.println(now.lengthOfMonth());
    System.out.println(now.lengthOfYear());
  }
}
