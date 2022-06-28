package com.datapipeline.juc;

import org.apache.flink.api.common.JobID;

import javax.xml.bind.ValidationEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class Demo {
  public static void main(String[] args) throws InterruptedException {
      Queue<Integer> queue = new ConcurrentLinkedQueue<>();
      queue.add(1);
      queue.add(2);
      queue.add(3);
      queue.add(4);
      queue.add(5);
           while(!queue.isEmpty()){
               Integer jobInfo = queue.poll();
               System.out.println("........"+jobInfo);
               synctest(jobInfo).thenAccept(tt->{
                   System.out.println("tt->"+tt);
                   System.out.println("jobInfo->"+jobInfo);  // 我在这里是能获取到 jobInfo 的值的
               });
           }
      TimeUnit.SECONDS.sleep(10000);
  }

  public static CompletableFuture<String> synctest(Integer t){
     return CompletableFuture.supplyAsync(()->{
         try {
             TimeUnit.SECONDS.sleep(10);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         return t+"->"+Thread.currentThread().getName();
     });
  }

  public static void test(ConcurrentLinkedQueue<String> queue) {
    ExecutorService threadPool = Executors.newFixedThreadPool(2);
    System.out.println("queue.size()->" + queue.size());
    CompletableFuture.runAsync(
        () -> {
          while (!queue.isEmpty()) {
            String poll = queue.poll();
            System.out.println("poll->" + poll + ",当前线程为->" + Thread.currentThread().getName());
              try {
                  TimeUnit.SECONDS.sleep(2);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
        },
        threadPool);
//    threadPool.shutdown();
  }


    public enum QueueJob {
        INSTANCE;

        Queue<String> queue = null;

        private synchronized Queue<String> getQueue() {
            if (queue == null) {
                System.out.println("获取队列");
                return new ConcurrentLinkedQueue<String>();
            } else {
                return queue;
            }
        }

        public static Integer total_slot = null;

        public synchronized void setTotal_slot() {
            if (total_slot == null) {
            }
        }

        public static synchronized void increase() {
            total_slot += 1;
        }

        public static synchronized void reduce() {
            total_slot -= 1;
        }

        public static boolean isFree() {
            return total_slot > 0;
        }
    }
}
