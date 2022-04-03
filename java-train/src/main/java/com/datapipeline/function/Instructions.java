package com.datapipeline.function;

// function这个包下主要放的是Java的常用函数式接口，函数式接口是jdk1.8之后出现的

/**
 * 概念
 * 函数式接口在Java中是指：有且仅有一个抽象方法的接口。(注意，是只有一个抽象方法，并不是要求只有一个方法）
 *
 * 函数式接口，即适用于函数式编程场景的接口。而Java中的函数式编程体现就是Lambda，所以函数式接口就是可
 * 以适用于Lambda使用的接口。只有确保接口中有且仅有一个抽象方法，Java中的Lambda才能顺利地进行推导。
 *
 * 备注：“语法糖”是指使用更加方便，但是原理不变的代码语法。例如在遍历集合时使用的for-each语法，其实
 * 底层的实现原理仍然是迭代器，这便是“语法糖”。从应用层面来讲，Java中的Lambda可以被当做是匿名内部 类的“语法糖”，但是二者在原理上是不同的。
 */
public class Instructions<T> {
    public static void main(String[] args) {
      Object obj =  method(()-> "hello");
        System.out.println(obj);

    }

    public static Object method(MyFunctionalInterface functionalInterface){
      return  functionalInterface.get();
    }
}
