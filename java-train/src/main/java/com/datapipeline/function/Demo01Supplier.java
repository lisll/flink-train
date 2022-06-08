package com.datapipeline.function;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

/**
 * java.util.function.Supplier 接口仅包含一个无参的方法: T get() 。
 * 用来获取一个泛型参数指定类型的对象数据。由于这是一个函数式接口，这也就意味着对应的Lambda表达式需要“对外提供”一个符合泛型类型的对象数据
 *
 * Java 8 中的 Supplier 是一个函数接口，无参数，返回值类型为泛型 T。Supplier 的使用比较简单，使用场景也比较单一。
 *
 * 通俗的来说Supplier相当于一个放东西的容器，你可以在这个容器里放一些没有入参的代码，然后返回T类型，当调用get()方法的时候才会去执行容器里的代码得到返回值。
 *
 *
 */
public class Demo01Supplier {
    public static void main(String[] args) throws Exception {
        test1();
        test2();
//        String msgA = "Hello ";
//        String msgB = "World ";
//        System.out.println(getSupplier(()-> msgA+msgB));
        int[] numbers = {100, 200, 300, 400, 500, -600, -700, -800, 900, -1000};
        Integer integer = arrayMax(() -> {
            int max = numbers[0];
            for (int i : numbers) {
                if (i > max) {
                    max = i;
                }
            }
            return max;
        });
        System.out.println("max ->"+integer);
    }
    public static void test1() throws InterruptedException {
        Supplier<String> supplier = () -> {
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            System.out.println("Supplier测试");
            return localDateTime.format(f1);
        };
        System.out.println("开始");
        System.out.println("当前时间：" + supplier.get());
        Thread.sleep(1000L);
        System.out.println("再次获取当前时间：" + supplier.get());
    }

    public static void test2(){
        Supplier<Double> randomValue = () -> Math.random();
        System.out.println("获取随机数：" + randomValue.get());
        System.out.println("再次获取随机数：" + randomValue.get());
    }

    public static String getSupplier(Supplier<String> supplier){
        return supplier.get();
    }

    public static Integer arrayMax(Supplier<Integer> supplier){
      return  supplier.get();
    }
}
