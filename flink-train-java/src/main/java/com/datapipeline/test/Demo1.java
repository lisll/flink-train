package com.datapipeline.test;

import java.util.concurrent.ConcurrentHashMap;

public class Demo1 {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, String> instance = new ConcurrentHashMap<>();
        instance.put(2,"2");
        instance.put(3,"3");
        instance.put(4,"4");
        System.out.println("before ->"+instance);
        // 如果之前不存在这个key值，就将这个key,即通过函数计算出来的value值放进这个map集合，否则直接返回原有的value值
        String s = instance.computeIfAbsent(2, k -> {
            String ss = "aa";
            return ss;
        });
        System.out.println("instance->"+instance);
    }
}
