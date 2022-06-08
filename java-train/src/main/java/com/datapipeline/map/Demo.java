package com.datapipeline.map;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Demo {
  public static void main(String[] args) {
    long t = 3600000;
//    System.out.println(t % 3600);
    //这里想要只保留分秒可以写成"mm:ss"
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    //这里很重要，如果不设置时区的话，输出结果就会是几点钟，而不是毫秒值对应的时分秒数量了。
    formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
    String hms = formatter.format(t);
    System.out.println(hms);
    Date startTime = new Date();
    System.out.println("-------"+startTime.getTime());
    System.out.println("Math"+Math.abs(-1));
    System.out.println(Math.abs(9));
    List<ProcessDbBean> processBean = new ArrayList<>();
    ProcessDbBean processDbBean1 = new ProcessDbBean(6, 8, new Date(), 5L, 6L, 0L,1L);
    ProcessDbBean processDbBean2 = new ProcessDbBean(7, 9, new Date(), 5L, 4L, 5L,3600000L);
    ProcessDbBean processDbBean3 = new ProcessDbBean(55, 66, new Date(), 5L, 5L, 5L,1000L);
    ProcessDbBean processDbBean4 = new ProcessDbBean(66, 88, new Date(), 5L, 5L, 5L,2000L);
    ProcessDbBean processDbBean5 = new ProcessDbBean(45, 54, new Date(), 5L, 5L, 5L,3000L);
    processBean.add(processDbBean1);
//    processBean.add(processDbBean2);
//    processBean.add(processDbBean3);
//    processBean.add(processDbBean4);
//    processBean.add(processDbBean5);
    List<Integer> list2 = new ArrayList<>();
    list2.add(3);
    list2.add(4);

    Long aLong1 = processBean.stream().map(ProcessDbBean::getTotalTime).reduce((a, b) -> a + b).get();
    System.out.println("总的校验时间为： "+aLong1);

    Long min = Collections.min(processBean.stream().filter(s->s!=null).map(s -> s.getSinkTotal()).collect(Collectors.toList()));
    System.out.println("min->"+min);
    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");

    Date sinkTotal = processBean.stream().min(Comparator.comparing(ProcessDbBean::getBatchTime)).get().getBatchTime();
    System.out.println("sinkTotal->"+sinkTotal);


    // 计算本批次所有表的总数
    Long aLong = processBean.stream().filter(s->s.getBatchSize()>0).map(r -> r.getBatchSize()).findFirst().orElse(99L);

    System.out.println("alone"+aLong);
    // 已经校验完成的表的个数
    long count = processBean.stream().count();
    if (count > 0) {
      List<Long> diff =
          processBean.stream()
              .filter((p -> p.getSrcTotal() != p.getSinkTotal()))
              .map(p -> p.getSrcTotal() - p.getSinkTotal())
              .collect(Collectors.toList());
      Long lessCount = diff.stream().filter(df->df>0).reduce((a, b) -> a + b).orElse(0L);
      Long moreCount =
          diff.stream().filter(df->df<0).map(m -> Math.abs(m)).reduce((a, b) -> a + b).orElse(0L);
        System.out.println(lessCount+"->"+moreCount+"->"+count);
    } else {
      System.out.println("count"+count);
    }
    long time = new Date().getTime();
    System.out.println("当前系统时间->"+time);
    System.out.println("差值"+(time-startTime.getTime()));

    // 2022-05-30 22:30:00
  }
}
