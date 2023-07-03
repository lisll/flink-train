package com.datapipeline.juc;

import java.util.concurrent.*;

public class ThreadPoolExecutorDemo {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,
                4,
                500,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2),
                r -> {
                    Thread thread = new Thread(r, "测试线程");
                    return thread;
                },
                new ThreadPoolExecutor.DiscardOldestPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        for (int i = 0; i < 15; i++) {
            final int taskId = i;
            threadPoolExecutor.submit(() -> {
                System.out.println("Task " + taskId + " is running, thread name = " + Thread.currentThread().getName() + ",currentTime=" + System.currentTimeMillis());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });
        }
        threadPoolExecutor.shutdown();
    }
}
