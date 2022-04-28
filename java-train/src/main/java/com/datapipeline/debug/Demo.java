package com.datapipeline.debug;

/**
 *  这个类主要为演示远程debug的demo
 */
public class Demo {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000*30);
            print();
            test();
        Person person = new Person();

        person.show();
    }

    public static void print(){
        System.out.println("this is print");
    }

    public static void test(){
        System.out.println("this is test");
    }
}
