package com.datapipeline.juc.lock;

import scala.tools.nsc.Global;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        AtomicInteger counter = new AtomicInteger();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 1000, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                (r) -> new Thread(r, counter.addAndGet(1) + "号"),
                new ThreadPoolExecutor.AbortPolicy());
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            try {
                System.out.println("裁判，比赛开始~~~~"+Thread.currentThread().getName());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.submit(new Runner(cyclicBarrier));
        }
        threadPoolExecutor.shutdown();
        System.out.println(Thread.currentThread().getName()+" 开始执行");
    }

    static class Runner extends Thread {
        private CyclicBarrier cyclicBarrier;


        public Runner(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            int sleepMills = ThreadLocalRandom.current().nextInt(1000);
            try {
                Thread.sleep(sleepMills);
                if(Thread.currentThread().getName().contains("3")){
                    int i = 1/0;
                }
                System.out.println(Thread.currentThread().getName() + " 选手已就位, 准备共用时： " + sleepMills + "ms" + ",准备好的选手个数为" + (cyclicBarrier.getNumberWaiting() + 1) + "个");
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName()+"选手准备过程中发生异常，需要重置");
                cyclicBarrier.reset();
                throw new RuntimeException(e);
            } finally {
                try {
                    cyclicBarrier.await(); // 阻塞等待所有的选手全部准备好
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
