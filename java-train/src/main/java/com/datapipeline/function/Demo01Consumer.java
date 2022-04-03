package com.datapipeline.function;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * java.util.function.Consumer 接口则正好与Supplier接口相反，它不是生产一个数据，而是消费一个数据， 其数据类型由泛型决定。
 * 抽象方法：accept
 * Consumer 接口中包含抽象方法 void accept(T t) ，意为消费一个指定泛型的数据。
 *
 * 默认方法：andThen
 * 如果一个方法的参数和返回值全都是 Consumer 类型，那么就可以实现效果:消费数据的时候，首先做一个操作， 然后再做一个操作，实现组合。
 * 而这个方法就是 Consumer 接口中的default方法 andThen 。
 */

public class Demo01Consumer {
    public static void main(String[] args) {
            consumerString((s)-> {
                int length = s.length();
                System.out.println(length);
            });

            consumerString(
                    (one)-> System.out.println(one.toLowerCase()),
                    (two) -> System.out.println(two.toUpperCase())
            );

        String[] array = { "大雄,男", "静香,女", "胖虎,男" };
        printInfo((one)->{
            System.out.print("姓名:"+one.split(",")[0]+",");},
                (two)->{System.out.println("性别:"+two.split(",")[1]);},
                array);

    }

    public static void consumerString(Consumer<String> consumer){
            consumer.accept("helloworld");
    }

    public static void consumerString(Consumer<String> befroe,Consumer<String> after){
            befroe.andThen(after).accept("HeLlo");
    }

    public static void printInfo(Consumer<String> one,Consumer<String> two,String[] array){
            for(String arr :array){
                one.andThen(two).accept(arr);
            }
    }

}
