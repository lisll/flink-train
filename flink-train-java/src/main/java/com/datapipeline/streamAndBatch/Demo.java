package com.datapipeline.streamAndBatch;

import java.util.stream.IntStream;

public enum Demo {
    ONE{
        @Override
        public int[] getOldSubtasks(int var1, int var2, int var3) {
            return new int[0];
        }

        public int getint(int a, int b){
            System.out.println("ONE 中的 getint 方法被调用");
            return a+b;
        }
    },
    TWO{
        @Override
        public int[] getOldSubtasks(int var1, int var2, int var3) {
            return new int[0];
        }

        public int getint(int a , int b){
            System.out.println("TWO中的getint方法被调用");
           return a+b;
       }
    };

    Demo() {
    }

    public abstract int[] getOldSubtasks(int var1, int var2, int var3);
    public abstract int getint(int a,int b);
}
