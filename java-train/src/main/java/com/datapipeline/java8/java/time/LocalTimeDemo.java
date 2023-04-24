package com.datapipeline.java8.java.time;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/** LocalTime 是 Java8 中新增的 时间类，主要包含了 小时、分钟、秒、纳秒 四个属性。
 *  LocalTime类中提供了丰富的API，帮助我们更加简便的操作时间对象。 */
public class LocalTimeDemo {

  public static void main(String[] args) {
    ArrayList<Integer> integers = new ArrayList<>();
    for(int i = 0;i<10;i++){
      integers.add(new Random().nextInt(10));
    }
    for (Integer integer : integers) {
      System.out.println(integer);
    }
    long count = new Random().ints(10, 20).limit(10).count();
    System.out.println(count);
//    IntStream.range(1, 3).forEach(System.out::println);
//    difference();
  }

  // 创建LocalTime类对象
  /**
   *  * 1.创建时间类对象
   *  *    1.1 获取当前时间
   *  *      LocalTime.now() : 获取默认时区下的系统时钟的时间
   *  *      LocalTime.now(Clock clock) : 获取指定时钟的时间
   *  *      LocalTime.now(ZoneId zone) : 获取指定时区的、默认系统时钟的时间
   *  *      【补充：获取所有时区信息的方式 ： ZoneId.getAvailableZoneIds()】
   *  *    1.2 获取指定时间
   *  *      * 小时的取值范围 : [0,23]
   *  *      * 分钟的取值范围 : [0,59]
   *  *      * 秒  的取值范围 : [0,59]
   *  *      * 纳秒的取值范围 : [0,999,999,999]
   *  *
   *  *      LocalTime.of(int hour, int minute) : 指定小时和分钟，秒和纳秒为0
   *  *      LocalTime.of(int hour, int minute, int second) : 指定小时、分钟、秒，纳秒为0
   *  *      LocalTime.of(int hour, int minute, int second, int nanoOfSecond) : 指定小时、分钟、秒、纳秒
   *  *      LocalTime.ofSecondOfDay(long secondOfDay) : 指定一天当中的多少秒，纳秒将被置为0
   *  *      LocalTime.ofNanoOfDay(long nanoOfDay) : 指定一天当中的多少纳秒
   *  *
   */
  public static void getLocalTime() {
    System.out.println(LocalTime.now());
    System.out.println(LocalTime.now(ZoneId.systemDefault()));
    System.out.println(LocalTime.now(Clock.systemUTC()));
    System.out.println(LocalTime.of(2,56));
    System.out.println(LocalTime.of(4,34,57));
    System.out.println(LocalTime.of(5,6,34,90999));
    System.out.println(LocalTime.ofSecondOfDay(7234));
    System.out.println(LocalTime.ofNanoOfDay(6789423));
  }

  // 2 获取LocalTime对象的时分秒
  /**
   *  * 2.获取时间对象的 时、分、秒、纳秒、信息
   *  *      getHour() : 获取小时信息
   *  *      getMinute() : 获取分钟信息
   *  *      getSecond() : 获取秒
   *  *      getNano() :  获取纳秒
   *  *
   */
  public static void getHours(){
    LocalTime now = LocalTime.now();
    System.out.println(now.getHour());
    System.out.println(now.getMinute());
    System.out.println(now.getSecond());
    System.out.println(now.getNano());
  }

  //3,指定LocalTime对象的时分秒信息
  /**
   * * 3.指定时间对象的 时、分、秒、纳秒
   *  *      withHour(int) : 指定小时
   *  *      withMinute(int) : 指定分钟
   *  *      withSecond(int) : 指定秒
   *  *      withNano(int) : 指定毫秒
   *  *
   *  *      with(TemporalAdjuster) : 时间矫正器
   *  *
   */
  public static void modifyLocalTime(){
    LocalTime now = LocalTime.now();
    System.out.println(now.withHour(5));
    System.out.println(now.withMinute(45));
    System.out.println(now.withSecond(34));
    TemporalAdjuster temporalAdjuster = temporal -> {
      LocalTime time = (LocalTime) temporal;
      time = time.withHour(time.getHour() + 1).withMinute(time.getMinute()+6).withSecond(time.getSecond()+3);
      return time;
    };
    System.out.println(now.with(temporalAdjuster));
  }

  // 4,增加/减少 LocalTime 对象的时分秒信息
  /**
   *  * 4.加上 或者 减去 时、分、秒、纳秒
   *  *     plusHours(long) : 加几个小时
   *  *     plusMinutes(long) : 加几分钟
   *  *     plusSeconds(long) : 加几秒钟
   *  *     plusNanos(long) : 加几个纳秒
   *  *
   *  *     minusHours(long) : 减几个小时
   *  *     minusMinutes(long) : 减几分钟
   *  *     minusSeconds(long) : 减几秒
   *  *     minusNanos(long) : 减几个纳秒
   *  *
   */
  public static void calcuLocalTime(){
//4.加上 或者 减去 小时、分钟、秒、纳秒
    LocalTime nowPlusOrMin = LocalTime.now();
    System.out.println("nowPlusOrMin = " + nowPlusOrMin);
    System.out.println("加两小时 = " + nowPlusOrMin.plusHours(2));
    System.out.println("加20分钟 = " + nowPlusOrMin.plusMinutes(20));
    System.out.println("加20秒 = " + nowPlusOrMin.plusSeconds(20));
    System.out.println("加1纳秒 = " + nowPlusOrMin.plusNanos(1));

    System.out.println();

    System.out.println("减两小时 = " + nowPlusOrMin.minusHours(2));
    System.out.println("减20分钟 = " + nowPlusOrMin.minusMinutes(20));
    System.out.println("减20秒 = " + nowPlusOrMin.minusSeconds(20));
    System.out.println("减1纳秒 = " + nowPlusOrMin.minusNanos(1));

    System.out.println("===========================");
  }
  // 5, LocalTime 对象的大小比较
  /**
   *  *  5.比较时间对象的大小
   *  *      time01.isAfter(time02) : time01 是否比 time02 晚
   *  *      time01.isBefore(time02) : time01 是否比 time02 早
   *  *
   */
  public static void equal(){
    LocalTime now = LocalTime.now();
    LocalTime of = LocalTime.of(23, 45);
    System.out.println(now.isBefore(of));
    System.out.println(now.isAfter(of));
  }

  // 6, LocalTiime 对象格式化为字符串
  /**
   *  *  6.时间对象 与 字符串的相互转化
   *  *    6.1 时间对象 转 字符串 format() + DateTimeFormatter类
   *  *        String format01 = localTime.format(DateTimeFormatter.ISO_TIME); // 21:56:17.05
   *  *        String format02 = localTime.format(DateTimeFormatter.ISO_LOCAL_TIME); // 21:56:17.05
   *  *        String format03 = localTime.format(DateTimeFormatter.ofPattern("HH--:--mm--:--ss")); // 21--:--56--:--17
   *  *
   *  *    6.2 字符串 转 时间对象 parse()
   *  *         LocalTime timeParse01 = LocalTime.parse("21:53:53", DateTimeFormatter.ofPattern("HH:mm:ss"));
   *  *
   */
  public static void parseString(){
    LocalTime now = LocalTime.now(Clock.systemUTC());
    System.out.println(now.format(DateTimeFormatter.ISO_TIME));
    System.out.println(now.format(DateTimeFormatter.ISO_LOCAL_TIME));
    System.out.println(now.format(DateTimeFormatter.ofPattern("HH--:--mm--:--ss")));
    System.out.println(LocalTime.parse("21:54:56",DateTimeFormatter.ofPattern("HH:mm:ss")));
  }

  //7, 计算两个时间对象之间的时间差
  /**
   *  *  7.获取两个时间对象之间的差 Duration
   *  *         Duration between = Duration.between(timeDuration01, timeDuration02);
   *  *         between.toDays()
   *  *         between.toHours() // 一共差多少个小时
   *  *         between.toMinutes() // 一共差多少个分钟
   *  *         between.toMillis() // 一共差多少毫秒
   *  *         between.toNanos() // 一共差多少纳秒
   *  *
   */
  public static void difference(){
    LocalTime now = LocalTime.now();
    LocalTime of = LocalTime.of(14, 6,23);
    System.out.println(Duration.between(now,of));
    System.out.println(Duration.between(now,of).toDays());
    System.out.println(Duration.between(now,of).toHours());
    System.out.println(Duration.between(now,of).toMinutes());
    System.out.println(Duration.between(now,of).toMillis());
    System.out.println(Duration.between(now,of).toNanos());
  }
}
