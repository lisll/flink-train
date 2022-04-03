package com.datapipeline.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Demo01Predicate {
    public static void main(String[] args) {
//        methodPredi((s)-> s.length()>5);

//       boolean b = methodAnd((s)->s.contains("A"),
//                (s)->s.contains("W"));
//        System.out.println(b);

//        System.out.println(methodOr(s -> s.contains("m"),s -> s.contains("A")));

        String[] array = { "迪丽热巴,女", "古力娜扎,男", "马尔扎哈,男", "赵丽颖,女","aaaa,女孩" };
        List<String> strings = mehtodStr(s -> s.split(",")[0].length() == 4, s -> s.split(",")[1].contains("女"), array);
        strings.forEach(s -> System.out.println(s));
    }

    // 条件判断的标准是传入的Lambda表达式逻辑
    public static void methodPredi(Predicate<String> predicate){
       boolean flag = predicate.test("dss");
        System.out.println("flag->"+flag);
    }

    public static boolean methodAnd(Predicate<String> one,Predicate<String> two){
         boolean flag =  one.and(two).test("AW");
        System.out.println("flag->"+flag);
        return flag;
    }

    public static boolean methodOr(Predicate<String> one,Predicate<String> two){
        return one.or(two).test("hellO");
    }


    public static List<String> mehtodStr(Predicate<String> one,Predicate<String> two,String[] arr){
        List<String> list = new ArrayList<>();
        for(String str : arr){
           if(one.and(two).test(str)){
               list.add(str);
           }
        }
        return list;
    }


}
