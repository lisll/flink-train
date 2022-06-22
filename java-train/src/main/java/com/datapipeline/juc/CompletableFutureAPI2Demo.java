package com.datapipeline.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureAPI2Demo {
  public static void main(String[] args) {
            thenApply();
//    handle();
  }

  public static void thenApply() {
    ExecutorService threadPool = Executors.newFixedThreadPool(3);
    CompletableFuture<Integer> exceptionally =
        CompletableFuture.supplyAsync(
                () -> {
                  // 暂停几秒钟线程
                  try {
                    TimeUnit.SECONDS.sleep(1);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  System.out.println("111");
                  return 1;
                },
                threadPool)
            .thenApply(
                f -> {
                  try {
                    TimeUnit.SECONDS.sleep(2);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  int i = 10 / 0; // 如果第二步出错就直接走whenComplete和exceptionally，而不会走第三步，并且这个异常会直接传递到whenComplete和exceptionally中
                  System.out.println("222222");
                  return f + 2;
                })
            .thenApply(
                f -> {
                  try {
                    TimeUnit.SECONDS.sleep(2);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  System.out.println("333333");
                  return f - 3;
                })
            .whenComplete(
                (v, e) -> {
                  System.out.println("运行 whenComplete");
                  if (e == null) {
                    System.out.println("----计算结果： " + v);
                  } else {
                    System.out.println("运行报错->" + e);
                  }
                })
            .exceptionally(
                e -> {
                  System.out.println("运行 exceptionally");
                  System.out.println("exceptionally中的报错->" + e.getMessage());
                  return null;
                });

    System.out.println(Thread.currentThread().getName() + "----主线程先去忙其它任务");
    threadPool.shutdown();
  }

  public static void handle() {
    ExecutorService threadPool = Executors.newFixedThreadPool(3);
    CompletableFuture.supplyAsync(
            () -> {
              // 暂停几秒钟线程
              try {
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              System.out.println("111");
              return 1;
            },
            threadPool)
        .handle(
            (f, e) -> {
              int i = 10 / 0; // 这个位置报错，不会走第二步但是还是会走第三步,并且第二步的返回值为null;
              System.out.println("222");
              return f + 2;
            })
        .handle(
            (f, e) -> {
              System.out.println("333");
              System.out.println("得到第二步中的结果->" + f); // 实际上因为第二步抛出了异常，所以第二步的结果为null;
              System.out.println("得到第二步中的异常->" + e);
              //            return f+3;     // 如果这个位置直接返回 f+3 那么会抛出空指针异常，如果直接返回3那么就不会出现这个问题；
              return e; // 将第二步中的异常作为第三步的结果，继续向后传递
            })
        .whenComplete(
            (v, e) -> { // 这个 e 其实指的是第三步中的异常
              System.out.println("运行 whenComplete");
              if (e == null) {
                System.out.println("----计算结果： " + v);
              } else {
                System.out.println("运行报错： " + e);
              }
            })
        .exceptionally(
            e -> {
              System.out.println("运行 exceptionally");
              System.out.println("exceptionally中的报错信息->" + e.getMessage());
              return null;
            });
    System.out.println(Thread.currentThread().getName() + "----主线程先去忙其它任务");
    threadPool.shutdown();
    System.out.println("执行结束.......");
  }
}
