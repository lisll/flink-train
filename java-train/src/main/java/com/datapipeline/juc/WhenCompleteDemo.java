package com.datapipeline.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WhenCompleteDemo {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(3);
        CompletableFuture<Void> exceptionally = CompletableFuture.supplyAsync(() -> {
//            try {TimeUnit.SECONDS.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
            return "hello";
        },service).thenAccept(s -> {
            System.out.println(s+"world");
        }).exceptionally(e -> {
            return null;
        });
        service.shutdown();
//        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
//            try {TimeUnit.SECONDS.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
//            System.out.println(Thread.currentThread().getName());
//            return 10;
//        },service).whenComplete((v,e)->{
//            if(e==null){
//                System.out.println("whenComplete"+Thread.currentThread().getName());
//                System.out.println(v+" 执行完成！");
//            }else{
//
//            }
//        }).exceptionally(e->{
//            System.out.println("运行 exceptionally");
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//            return null;
//        });
//        service.shutdown();
    }
}
