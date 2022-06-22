package com.datapipeline.juc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 在该例子中，因为future1、future2、future3的返回值都是CompletableFuture<String>，
 * 所以anyOf的返回的Object一定也是 String 类型。
 *
 * 并且在 3 个 future 中，future2 睡眠时间最短，会最先执行完成，
 * anyOfFuture.get()获取的也就是 future2 的内容。future1、future3 的 返回结果被丢弃了
 */
public class AnyOfDemo {
    public static void main(String[] args) {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "任务1";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(7);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "任务2";
        });
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "任务3";
        });
        System.out.println("before->" + new SimpleDateFormat("yyyy-MM-dd mm:hh:ss").format(new Date()));
        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(future1, future2, future3);
        System.out.println(anyOfFuture.join());
        System.out.println("after->" + new SimpleDateFormat("yyyy-MM-dd mm:hh:ss").format(new Date()));
    }
}
