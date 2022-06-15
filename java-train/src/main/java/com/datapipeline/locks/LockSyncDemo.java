package com.datapipeline.locks;

public class LockSyncDemo {
//    Object object = new Object();
//    public void m1(){
//        synchronized (object){
//            System.out.println("----------m1 synchronized code");
//        }
//    }

    public synchronized  void m2(){
        System.out.println("----------m2 synchronized");
    }
    public static synchronized void m3(){
        System.out.println("----------m3 static  synchronized");
    }
    public static void main(String[] args) {

    }
}
