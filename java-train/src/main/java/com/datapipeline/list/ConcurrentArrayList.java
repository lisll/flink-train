package com.datapipeline.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConcurrentArrayList {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = Collections.synchronizedList(new ArrayList<Integer>());

        Runnable runnable = () -> {
            for (int i = 0; i < 10000; i++) {
                list.add(i);
            }
        };

        for (int i = 0; i < 2; i++) {
            new Thread(runnable).start();
        }

        Thread.sleep(500);
        System.out.println(list.size());
    }
}
