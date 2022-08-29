package com.datapipeline.utils;

public class StaticFinal {
    public static final int A = 8;
    public static int B = 9;

    public static final Object O = new Object();

    static {
        System.out.println("如果执行了，证明类初始化了");

    }

}

class MyTest {
    public static void main(String[] args) {
        System.out.println(StaticFinal.B);

    }
}
