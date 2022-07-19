package com.datapipeline.locks;

import java.util.concurrent.locks.ReentrantLock;

public class SaleTicketDemo {

  static class Ticket {
    private int numerTicket=50;
    ReentrantLock lock = new ReentrantLock(true);
    private void sale() {
      lock.lock();
     try{
       if (numerTicket > 0) {
         System.out.println(
                 Thread.currentThread().getName()
                         + "卖出第：\t"
                         + (numerTicket--)
                         + "\t 还剩余:"
                         + numerTicket);
         // 不加这个sleep时间，很有可能无法模拟出多线程的问题
         try {
           Thread.sleep(10);
         } catch (InterruptedException e) {
           e.printStackTrace();
         }
       }
     }finally {
       lock.unlock();
     }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Ticket ticket = new Ticket();
    new Thread(()->{for(int i=0;i<50;i++){ticket.sale();}},"a").start();
    new Thread(()->{for(int i=0;i<50;i++){ticket.sale();}},"b").start();
    new Thread(()->{for(int i=0;i<50;i++){ticket.sale();}},"c").start();
  }
}
