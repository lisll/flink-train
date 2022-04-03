package com.datapipeline.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class DemoNormalFilter {
    public static void main(String[] args) {
        //第一支队伍
        ArrayList<String> one = new ArrayList<>();
        Collections.addAll(one, "迪丽热巴", "宋远桥", "苏星河", "石破天", "石中玉", "老子", "庄子", "洪七公");
        //第二支队伍
        ArrayList<String> two = new ArrayList<>();
        Collections.addAll(two, "古力娜扎", "张无忌", "赵丽颖", "张三丰", "尼古拉斯赵四", "张天爱", "张二狗");

        Stream<String> limit = one.stream().filter(name -> name.length() == 3).limit(3);
        Stream<String> skip = two.stream().filter(name -> name.startsWith("张")).skip(2);
        Stream<String> concat = Stream.concat(limit, skip);
        concat.forEach(System.out::println);
//        List<String> list = new ArrayList<>();
//        Collections.addAll(list, "Java", "C", "Python", "Hadoop", "Spark");
//        list.stream()
//                .filter((s) -> s.length() > 5)
//                .filter((s) -> s.length() >= 4)
//                .forEach((s) -> System.out.println(s));
//        System.out.print("筛选前的集合：");
//        for (String s : list) {
//            System.out.print(s + "，");
//        }
//        System.out.println();
//
//        System.out.print("经过条件1筛选后的集合：");
//        for (String s : list) {
//            if (s.length() > 5) {
//                System.out.print(s + "，");
//            }
//        }
//        System.out.println();
//
//        System.out.print("经过条件2筛选后的集合：");
//        for (String s : list) {
//            if (s.length() >= 4) {
//                System.out.print(s + "，");
//            }
//        }
//        System.out.println();
    }
}
