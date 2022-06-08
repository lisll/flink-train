package com.datapipeline.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFutureAPIDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        group1();
    }

    // 获得结果和触发计算
    public static void group1 () {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            //暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "abc";
        });
        //暂停几秒钟线程    如果不暂停这两秒，那么结果就是 true completeValue    如果暂停，那么结果就是 false	abc
//        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
//        System.out.println(completableFuture.getNow("xxx"));  // 结果是 xxx
        System.out.println(completableFuture.complete("completeValue")+"\t"+completableFuture.join());
    }
}
