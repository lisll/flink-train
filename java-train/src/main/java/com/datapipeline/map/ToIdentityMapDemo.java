package com.datapipeline.map;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

public class ToIdentityMapDemo {
    public static void main(String[] args) {
     test1();
    }
    public static void test1(){
        ProcessDbBean processDbBean1 = new ProcessDbBean(1, 1, null, 1L, 1L, 1L, 1L);
        ProcessDbBean processDbBean2 = new ProcessDbBean(2, 2, null, 1L, 1L, 1L, 1L);
        ProcessDbBean processDbBean3 = new ProcessDbBean(3, 3, null, 1L, 1L, 1L, 1L);
        List<ProcessDbBean> processDbBeans = Arrays.asList(processDbBean1, processDbBean2, processDbBean3);

        Map<Integer, ProcessDbBean> integerProcessDbBeanMap = toIdentityMap(processDbBeans, ProcessDbBean::getValidatorId);
//        System.out.println("");

        List<Pair> list = new ArrayList<>();
        list.add(new Pair("version",7.7,"a"));
        list.add(new Pair("version",7.9,"b"));
        list.add(new Pair("version",8.0,"c"));
        Map<String, String> collect = list.stream().collect(Collectors.toMap(Pair::getVersion, Pair::getEmail, (oldValue, newValue) -> {
            System.out.println(".........");
            return newValue;
        }));
        System.out.println("....");
        Set<Map.Entry<String, String>> entries = collect.entrySet();


    }


    /**
     * identity() 表示得到对象本身
     * @param elements
     * @param keyMapper
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> toIdentityMap(Collection<V> elements, Function<V, K> keyMapper) {
        if (isEmpty(elements)) {
            return emptyMap();
        }
        return elements.stream()
                .collect(
                        Collectors.toMap(
                                keyMapper, identity(), (a, b) -> b, () -> new HashMap<>(elements.size(), 1)));
    }

    public static class Pair{
        private String version;
        private double money;
        private String email;
        public Pair(String version, double money,String email){
            this.version = version;
            this.money = money;
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }
    }

}
