package com.datapipeline.locks;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {

    // 悲观锁的调用方式
    public static synchronized void m1(){
        // 加锁后的逻辑
    }

    static ReentrantLock lock = new ReentrantLock();
    public static void m2(){
        lock.lock();
        try {
            // 操作同步资源
        }finally {
            lock.unlock();
        }
    }

    // 乐观锁的调用方式
    // 保证多个线程使用的是同一个AtomicInteger
    public static void m3(){
        AtomicInteger ato = new AtomicInteger();
        ato.incrementAndGet();


        

    }

}
