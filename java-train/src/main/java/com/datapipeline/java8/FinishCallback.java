package com.datapipeline.java8;

public interface FinishCallback {

  void get(String name, Thread thread);

  FinishCallback FINISH_CALLBACK = (x, y) -> {};
}
