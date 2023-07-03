package com.datapipeline.map;

import java.util.*;

public class HashMapDemo {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        Integer li = map.put("li", 12);
        Integer u = map.put("u", 90);
        Integer li1 = map.put("li", 13);
        Set<String> strings = map.keySet();
        Collection<Integer> values = map.values();
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> next = iterator.next();
            System.out.println(next.getKey());
            System.out.println(next.getValue());
        }

    }
}
