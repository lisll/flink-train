package com.datapipeline.streamAndBatch;

import org.apache.flink.hive.reshaded.parquet.it.unimi.dsi.fastutil.ints.IntSets;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;

public class Test {
    enum Color {GREEN, RED, BLUE}
    public static void main(String[] args) {
        printName(Color.BLUE);
        printName(Color.RED);
    }

    public static void printName(Color color) {
        switch (color) {
            //无需使用Color进行引用
            case BLUE:
                System.out.println("蓝色");
                break;
            case RED:
                System.out.println("红色");
                break;
            case GREEN:
                System.out.println("绿色");
                break;
        }
    }
}
