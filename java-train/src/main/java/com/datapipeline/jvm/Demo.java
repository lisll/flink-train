package com.datapipeline.jvm;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Demo {
    public static void main(String[] args) {
       List<Long> list = new ArrayList<>();
       for(int i =0 ;i< 5; i++){
           Date date = new Date();
           list.add(date.getTime());
           list.add(date.getTime());
           list.add(date.getTime());
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
       list.forEach(s-> System.out.println(s));
        long date = list.stream().max((o1, o2) -> -1).get();
//        System.out.println(Collections.max(list));
        System.out.println("date->"+date);
        Long max = Collections.max(list);
        System.out.println("max->"+max);
        long count = list.stream().filter(s -> s.equals(Collections.max(list))).count();
        System.out.println(count);
    }

    public enum QueueJob {
        INSTANCE;

        private Queue<String> getQueue() {
            return new ConcurrentLinkedQueue();
        }

        public static Integer total_slot = null;

        public void setTotal_slot(){
           if(total_slot==null){
               total_slot = 5;
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
