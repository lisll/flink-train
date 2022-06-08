package com.datapipeline.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureAPI2Demo {
    public static void main(String[] args)
    {
//        thenApply();
        handle();
    }


    public static void thenApply(){
        ExecutorService service = Executors.newFixedThreadPool(3);
        CompletableFuture<Integer> exceptionally = CompletableFuture.supplyAsync(() -> {
            //暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("111");
            return 1;
        },service).thenApply(f -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("222222");
            int i = 10/0;  // 如果第二步出错就直接走exceptionally，而不会走第三步
            return f+2;
        }).thenApply(f -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("333333");
            return f-3;
        }).whenComplete((v, e) -> {
            System.out.println("运行 whenComplete");
            if (e == null) {
                System.out.println("----计算结果： " + v);
            }
        }).exceptionally(e -> {
            System.out.println("运行 exceptionally");
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName()+"----主线程先去忙其它任务");
        service.shutdown();
    }

    public static void handle(){
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        CompletableFuture.supplyAsync(() ->{
            //暂停几秒钟线程
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println("111");
            return 1;
        },threadPool).handle((f,e) -> {
//            int i=10/0;  // 这个位置报错，不会走第二步但是还是会走第三步
            System.out.println("222");
            return f + 2;
        }).handle((f,e) -> {
            System.out.println("333");
            return f + 3;
        }).whenComplete((v,e) -> {
            System.out.println("运行 whenComplete");
            if (e == null) {
                System.out.println("----计算结果： "+v);
            }
        }).exceptionally(e -> {
            System.out.println("运行 exceptionally");
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName()+"----主线程先去忙其它任务");

        threadPool.shutdown();
    }
}
