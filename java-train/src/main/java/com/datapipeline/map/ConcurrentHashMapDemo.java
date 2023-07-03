package com.datapipeline.map;

import com.google.common.collect.MapMaker;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentHashMapDemo {
    public static void main(String[] args) {

        ConcurrentHashMap chm = new ConcurrentHashMap();
        chm.putIfAbsent("abc",1);


    }
}
