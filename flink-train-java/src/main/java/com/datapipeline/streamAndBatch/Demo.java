package com.datapipeline.streamAndBatch;

import java.util.stream.IntStream;

public enum Demo {
    ONE{
        @Override
        public int[] getOldSubtasks(int var1, int var2, int var3) {
            return new int[0];
        }

        public int getint(int a, int b){
            return a+b;
        }
    },
    TWO{
        @Override
        public int[] getOldSubtasks(int var1, int var2, int var3) {
            return new int[0];
        }

        public int[] getint(int a , int b){
           return IntStream.range(a,b).toArray();
       }
    };

    Demo() {
    }

    public abstract int[] getOldSubtasks(int var1, int var2, int var3);
}
