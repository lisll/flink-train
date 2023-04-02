package com.datapipeline.juc;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 总结： 1，allOf 函数是当所有的Future都返回结果时，才触发下一步计算 2，任意一个Future报错，那么返回的值都为null
 * 3，多个Future报错时，返回的异常报错信息和最开始的那个报错一样 例如： 下面的案例中，当循环到3时报了一个异常，循环到5时也报了一个异常，那么返回的异常就是3那个异常
 */
public class AllofDemo {
  public static void main(String[] args) throws Exception {

    // 1，模拟多线程从不同的网站拉取数据
    List<String> webPages = Arrays.asList("1", "2", "3", "4", "5", "6");
    System.out.println("遍历前->" + new SimpleDateFormat("yyyy-MM-dd mm:hh:ss").format(new Date()));
    List<CompletableFuture> pageFutures =
        webPages.stream()
            .map(webPageLink -> downLoadWebPages(webPageLink))
            .collect(Collectors.toList());
    System.out.println("遍历后->" + new SimpleDateFormat("yyyy-MM-dd mm:hh:ss").format(new Date()));
    System.out.println("pageFutures->" + pageFutures.size());

    // 2， 异步执行，同步搜集结果
    CompletableFuture<Void> allFutures =
        CompletableFuture.allOf(pageFutures.toArray(new CompletableFuture[0]));

    // 3.1 无返回值的Future，非链式编程
    //    CompletableFuture<Void> runFuture =
    //        allFutures.thenRun(
    //            () -> pageFutures.stream().map(page -> page.join()).collect(Collectors.toList()));
    //    CompletableFuture<Void> future =
    //        runFuture.whenComplete(
    //            (v, t) -> {
    //              System.out.println("r-->" + v);
    //              System.out.println("t---> " + t);
    //            });
    //     3.2 无返回值的Future，链式编程
    CompletableFuture<Void> future =
        allFutures
            .thenRun(() -> pageFutures.forEach(CompletableFuture::join))
            .whenComplete(
                (v, t) -> {
                  System.out.println("v->" + v);
                  System.out.println("t->" + t);
                });

    /** 无返回值的Future的，主要是利用是否有异常来判断所有的结果是否都正常执行了 */

    // 3.3 有返回值的Future，非链式编程，可以在 whenComplete中得到 v
    //        CompletableFuture<List<Object>> applyFuture =
    //            allFutures.thenApply(
    //                value -> pageFutures.stream().map(page ->
    // page.join()).collect(Collectors.toList()));
    //        CompletableFuture<List<Object>> future2 =
    //            applyFuture.whenComplete(
    //                (v, t) -> {
    //                  System.out.println("v-->" + v);
    //                  System.out.println("t--->" + t);
    //                });
    // 3.4 有返回值的Future,采用链式编程 ，结果和非链式编程结果是一样的；
    //    CompletableFuture<List<Object>> future =
    //        allFutures
    //            .thenApply(
    //                value -> pageFutures.stream().map(p -> p.join()).collect(Collectors.toList()))
    //            .whenComplete(
    //                (v, t) -> {
    //                  System.out.println("v->" + v);
    //                  System.out.println("t->" + t);
    //                });
    /** 有返回值的Future：可以在v中得到结果 */
    // 得到结果
    System.out.println("before->" + new SimpleDateFormat("yyyy-MM-dd mm:hh:ss").format(new Date()));
    System.out.println("future->" + future.get());
    System.out.println("after->" + new SimpleDateFormat("yyyy-MM-dd mm:hh:ss").format(new Date()));
  }

  public static CompletableFuture<String> downLoadWebPages(final String links) {
    ExecutorService service = Executors.newFixedThreadPool(10);
    CompletableFuture<String> stringCompletableFuture =
        CompletableFuture.supplyAsync(
            () -> {
              System.out.println("当前线程为:->" + Thread.currentThread().getName());
              if (links.contains("5")) {
                try {
                  TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                return "world" + links;
              }
              if (links.contains("1")) {
                try {
                  //                  int a = 10 / 0;
                  TimeUnit.SECONDS.sleep(8);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
              if (links.contains("3")) {
                int[] a = new int[0];
                //                a[2] = 3;
                try {
                  TimeUnit.SECONDS.sleep(6);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
              return "hello" + links;
            },
            service);
    service.shutdown();
    return stringCompletableFuture;
  }
}
