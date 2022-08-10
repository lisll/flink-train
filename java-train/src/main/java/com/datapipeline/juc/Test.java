package com.datapipeline.juc;

import com.datapipeline.java8.BlogPost;
import org.apache.flink.api.common.JobID;

import java.util.*;
import java.util.concurrent.*;

public class Test {
    public static void main(String[] args) throws InterruptedException {

        DiffType diff = DiffType.DIFF;
        test3(diff);

//        for(Integer i : set1){
//            System.out.println("i->"+i);
//            if(set.contains(i)){
//                System.out.println("????????");
//            }
//        }
    }

    public static void test3(DiffType diffType){
        switch (diffType){
            case LESS:
                System.out.println("less");
                System.out.println("less++");
            case MORE:
                System.out.println("more");
                System.out.println("more++");
            case DIFF:
                System.out.println("diff");
                System.out.println("diff++");
                System.out.println("...............");
        }
    }

    public void test(){
        BlogPost blogPost = new BlogPost() ;
        new BlogPost(){

        };
    }


    public static void removeList(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(8);
        integers.add(9);
        integers.add(10);
        System.out.println(integers.size());
        int size = integers.size();
        for(int i = 0;i<size;i++){
            System.out.println("i->"+i);
            integers.remove(0);
        }
        System.out.println(integers.size());
    }


    public static CompletableFuture<String> submit(String i){
      return CompletableFuture.supplyAsync(()->{
          try {
              TimeUnit.SECONDS.sleep(10);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
          return i;
        });
    }
    public static void test3(ConcurrentLinkedQueue<String> queue) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(15);
        List<CompletableFuture<Void>> futures = new ArrayList<CompletableFuture<Void>>();
         CompletableFuture.supplyAsync(() -> {
            while(!queue.isEmpty()){
                String poll = queue.poll();
                CompletableFuture<Void> future = submit(poll).thenAccept(jobID -> {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println("jobID->"+jobID);
                });
              futures.add(future);
                System.out.println("futures.size()"+futures.size());
              CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                      .thenRun(()->futures.forEach(CompletableFuture::join))
                      .whenComplete((v,e)->{
                          System.out.println("v->"+v);
                          System.out.println("e->"+e);
                      });
            }
        return null;
        },threadPool).exceptionally(e->{
             System.out.println("eeeeee->"+e);
            return null;
         });
         threadPool.shutdown();
         Thread.sleep(100000);
    }

    public static void test2(){
        ExecutorService threadPool = Executors.newFixedThreadPool(15);
        long before = System.currentTimeMillis();
        for(int i=0;i<13;i++){
            CompletableFuture.runAsync(()->{
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            },threadPool);
        }
        System.out.println( (System.currentTimeMillis()-before)/1000);
        threadPool.shutdown();
    }
    public static void test1(){
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        CompletableFuture.runAsync(()->{
            int b = 0;
            for(int i =0;i<3;i++){
                System.out.println(Thread.currentThread().getName());
                System.out.println(i);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                int a = 1/0;
            }

            try{
                int i = 1/0;
            }catch (Exception exception){
                System.out.println("................");
            }

        },threadPool).exceptionally(e->{
            System.out.println(">>>>>"+e);
            if(e !=null){
                System.out.println(e.toString());
            }
            return null;
        });
    }

    public enum DiffType {
        MORE,
        LESS,
        DIFF,
        COUNT_DIFF
    }
}
