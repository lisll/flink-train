package com.datapipeline.juc.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/** 参考文章： https://www.cnblogs.com/liumy/p/11632878.html https://www.tpvlog.com/article/30 */
public class AtomicReferenceTest {

  public static void main(String[] args) throws Exception {
    AtomicReference<Integer> ref = new AtomicReference<>(new Integer(1000));
    List<Thread> list = new ArrayList<>();
    for (int i = 0; i < 58; i++) {
      Thread thread = new Thread(new Task(ref), "Thread--" + i);
      list.add(thread);
      thread.start();
    }
    for (Thread thread : list) {
      System.out.println(thread.getName());
      thread.join();
    }
    System.out.println(ref.get());
  }
}
// 该示例并没有使用锁，而是使用自旋+CAS的无锁操作保证共享变量的线程安全。1000个线程，
// 每个线程对金额增加1，最终结果为2000，如果线程不安全，最终结果应该会小于2000。
class Task implements Runnable {

  private final AtomicReference<Integer> ref;

  Task(AtomicReference<Integer> ref) {
    this.ref = ref;
  }

  @Override
  public void run() {
    for (; ; ) {
      Integer oldV = ref.get();
      System.out.println(Thread.currentThread().getName() + ":" + oldV);
      if (ref.compareAndSet(oldV, oldV + 1)) {
        break;
      }
    }
  }
}
