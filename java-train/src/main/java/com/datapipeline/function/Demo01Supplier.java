package com.datapipeline.function;

import java.util.function.Supplier;

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
