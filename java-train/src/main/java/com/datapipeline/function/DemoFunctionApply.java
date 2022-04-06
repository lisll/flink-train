package com.datapipeline.function;

import java.util.function.Function;

/**
 * 参考文章： https://www.cnblogs.com/liyihua/p/12286100.html
 *
 * java.util.function.Function<T,R> 接口用来根据一个类型的数据得到另一个类型的数据，前者称为前置条件，后者称为后置条件。
 * 抽象方法：apply
 * Function 接口中最主要的抽象方法为: R apply(T t) ，根据类型T的参数获取类型R的结果。 使用的场景例如:将 String 类型转换为 Integer 类型。
 *
 * 默认方法：andThen
 * Function 接口中有一个默认的 andThen 方法，用来进行组合操作。
 * 该方法同样用于“先做什么，再做什么”的场景，和 Consumer 中的 andThen 差不多:
 *
 */
public class DemoFunctionApply {
    public static void main(String[] args) {
//        method(s -> Integer.parseInt(s));
//        methodAndThen(s -> Integer.parseInt(s),integer -> integer+=100);
        methodThen(s -> s.split(",")[1], s -> Integer.parseInt(s),integer -> integer+=100,"赵丽丽,18");
    }
    public static void method(Function<String,Integer> function){
        Integer apply = function.apply("30");
        System.out.println(apply+20);
    }

    public static void methodAndThen(Function<String,Integer> one,Function<Integer,Integer> two){
        Integer apply = one.andThen(two).apply("40");
        System.out.println(apply+10);
    }


    /**
     * 1.将字符串截取数字年龄部分，得到字符串;
     * 2.将上一步的字符串转换成为int类型的数字;
     * 3.将上一步的int数字累加100，得到结果int数字
     * @param one
     * @param two
     * @param after
     * @param source
     */
   public static void methodThen(Function<String,String> one,Function<String,Integer> two,Function<Integer,Integer> after,String source){
       Integer apply = one.andThen(two).andThen(after).apply(source);
       System.out.println(apply+8);
   }
}
