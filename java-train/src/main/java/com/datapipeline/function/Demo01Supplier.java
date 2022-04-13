package com.datapipeline.function;

import java.util.function.Supplier;

/**
 * java.util.function.Supplier 接口仅包含一个无参的方法: T get() 。
 * 用来获取一个泛型参数指定类型的对象数据。由于这是一个函数式接口，这也就意味着对应的Lambda表达式需要“对外提供”一个符合泛型类型的对象数据
 */
public class Demo01Supplier {
    public static void main(String[] args) {
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
    public static String getSupplier(Supplier<String> supplier){
        return supplier.get();
    }

    public static Integer arrayMax(Supplier<Integer> supplier){
      return  supplier.get();
    }
}
