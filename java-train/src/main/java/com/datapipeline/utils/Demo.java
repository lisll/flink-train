package com.datapipeline.utils;

import java.util.Random;

public class Demo {
    public static String values = "静态变量";
    public static final int A = 8;
    public static final String FINAL_VALUE = "静态常量";
    public static final int FINAL_VALUE_INT = new Random(10).nextInt();
    public static void main(String[] args) {
        System.out.println(Demo.A);
    }
    static {
        System.out.println("类加载了");
    }

}
