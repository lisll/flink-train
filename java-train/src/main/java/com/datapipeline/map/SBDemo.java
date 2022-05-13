package com.datapipeline.map;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class SBDemo {
    public static void main(String[] args) {
        // 软引用
        String a = new String("hello world");
        ReferenceQueue<String> queue = new ReferenceQueue<String>();
        SoftReference<String> ref = new SoftReference<>(a, queue);
//        WeakReference<String> ref = new WeakReference<>(a,queue);
        a = null;
        System.gc();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String value = ref.get();
        System.out.println(value);
        System.out.println(ref.isEnqueued());
        System.out.println(queue.poll());



        String s1 = "Monday";

//        String s2 = "Monday1";

        String s2 = new String("Monday").intern();
//        s2 = s2.intern();
        // == 号比较的是地址
        // equals 比较的是值
        if (s1 == s2) {System.out.println("s1 == s2");}
        else{ System.out.println("s1 != s2");}
        if(s1.equals(s2)){
            System.out.println("s1 equals s2");
        }else{
            System.out.println("s1 not equals s2");
        }

        int a1=1;

        int a2=1;

        Integer b1 =new Integer (1);

        Integer b2 =new Integer (1);

        if(a1==a2){
            System.out.println("a1 == a2");
        }else{
            System.out.println("a1 != a2");
        }

        if(b1==b2){
            System.out.println("b1 == b2");
        }else{
            System.out.println("b1 != b2");
        }
        if(b1.equals(b2)){
            System.out.println("b1 equals b2");
        }


    }

}
