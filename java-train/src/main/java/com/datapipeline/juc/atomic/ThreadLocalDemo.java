package com.datapipeline.juc.atomic;

public class ThreadLocalDemo {

    static ThreadLocal<String> local = new ThreadLocal<>();
    public static void main(String[] args) {
        local.set("1");
        local.set("2");
        System.out.println(local.get());

    }
}
