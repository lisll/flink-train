package com.datapipeline.juc.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一：CountDwonLatch概述
 * 1,CountDownLatch一般用作多线程倒计时计数器，强制它们等待其他一组（CountDownLatch的初始化决定）任务执行完成。
 * 2，有一点要说明的是CountDownLatch初始化后计数器值递减到0的时候，不能再复原的，
 * 这一点区别于Semaphore，Semaphore是可以通过release操作恢复信号量的。
 * 二：使用原理
 * 1，创建CountDownLatch并设置计数器值。
 * 2，启动多线程并且调用CountDownLatch实例的countDown()方法。
 * 3，主线程调用 await() 方法，这样主线程的操作就会在这个方法上阻塞，直到其他线程完成各自的任务，count值为0，
 * 停止阻塞，主线程继续执行。
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch doneSignal = new CountDownLatch(10);
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            // create and start threads
            cachedThreadPool.execute(new Worker(doneSignal, i));
        }
        // don't let run yet
        System.out.println("do something else 1");
        // wait for all to finish
        doneSignal.await();
        System.out.println("===========================count: " + doneSignal.getCount());
        System.out.println("do something else 2");
        cachedThreadPool.shutdown();
        Object o = new Object();

    }


    static class Worker implements Runnable {

        private final CountDownLatch doneSignal;
        private final int i;

        Worker(CountDownLatch doneSignal, int i) {
            this.doneSignal = doneSignal;
            this.i = i;
        }

        @Override
        public void run() {
            try {
                doWork();
                System.out.println("i = " + i + ", " + doneSignal.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                doneSignal.countDown();
            }
        }

        void doWork() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("do work!");
        }
    }
}
