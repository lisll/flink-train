package com.datapipeline.map;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 *
 * 如果指定的key 不存在关联的值或者返回 Null,那么就会试着去执行传入的mappingFunction。
 *
 * default V computeIfAbsent(K key,Function<? super K, ? extends V> mappingFunction) {
 *         Objects.requireNonNull(mappingFunction);
 *         V v;
 *         //获取的的value不存在
 *         if ((v = get(key)) == null) {
 *             V newValue;
 *             //对传入key 应用 Function
 *             if ((newValue = mappingFunction.apply(key)) != null) {
 *                 //新value放入map
 *                 put(key, newValue);
 *                 //返回新的value
 *                 return newValue;
 *             }
 *         }
 *         //map中存在则直接返回
 *         return v;
 *     }
 */
public class ConcurrentHashMapDemo {
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> conc = new ConcurrentHashMap<>();
        String put = conc.put("hello", "worlds");
        System.out.println("put->"+put);

//        conc.computeIfAbsent("hello", (key)->key.toUpperCase());
//        conc.forEach(4,(key,value)-> System.out.println("key:"+key+",value:"+value));
//
//        //实现计算功能统计字符串出现次数
        ConcurrentHashMap<String, AtomicInteger> map = new ConcurrentHashMap<>();
        List<String> source = Arrays.asList("hello", "world", "hello", "welcome", "hello", "hello", "welcome", "simon");
        for(String str : source){
            map.computeIfAbsent(str,key->new AtomicInteger()).getAndIncrement();
        }
        System.out.println(map);
    }
}
