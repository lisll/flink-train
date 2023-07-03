package com.datapipeline.map;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapDemo1 {
    public static void main(String[] args) {
        ConcurrentHashMap chm = new ConcurrentHashMap(32);
        for (int i = 0; i < 47; i++) {
            chm.put("abc" + i, i);
            System.out.println(chm.size());
        }
        new Thread(() -> {
            chm.put("通话", "11");
            System.out.println("-----------");
        }).start();

        new Thread(() -> {
            chm.put("重地", "22");
            System.out.println("===========");
        }).start();

        new Thread(() -> {
            chm.put("abc5", "xx");
            System.out.println("...............");
        }).start();
    }
}
