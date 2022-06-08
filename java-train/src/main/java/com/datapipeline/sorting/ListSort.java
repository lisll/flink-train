package com.datapipeline.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListSort {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(199);
        list.add(20);
        list.add(14);
        list.add(3);
        list.stream().sorted().forEach(s-> System.out.println(s));
        list.stream().sorted(Comparator.reverseOrder()).forEach(s-> System.out.println(s));
        String ss = "jdbc:mysql://dp-thrall-mysql:3306";
        System.out.println(ss.indexOf("/",13));
    }
}
