package com.datapipeline.thread;

public class DaemonThread{

  public static void main(String[] args) {
    Thread user = new Thread(() -> {
      int i = 10;
      while (true) {
        if(i-- == 0) break;
        try {
          Thread.sleep(300);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("user");
      }
    });
    user.start();

    Thread daemon = new Thread(() -> {
      while (true) {
        try {
          Thread.sleep(300);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("daemon");
      }
    });
    daemon.setDaemon(true);
    daemon.start();

    System.out.println("main out");
  }

}
