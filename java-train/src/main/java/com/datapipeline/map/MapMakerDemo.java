package com.datapipeline.map;

import com.google.common.collect.MapMaker;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class MapMakerDemo {
    /**
     * concurrencyLevel:设置同时修改的并发线程数
     * initialCapacity : 设置map的初始容量大小
     */
    private static final Map<String, Object> WEB_CONFIG_CACHE =
            new MapMaker().concurrencyLevel(5).initialCapacity(50).makeMap();
    public static void main(String[] args) {
        new MapMaker().concurrencyLevel(32).initialCapacity(10).weakKeys().weakValues().makeMap();
//        ConcurrentMap<String, String> testMap = new MapMaker().concurrencyLevel(32).weakValues().expiration(
//                3, TimeUnit.SECONDS).makeComputingMap(new Function<String, String>() {
//            /**
//             * 这里就是绑定的根据key没找到value的时候触发的function，
//             * 可以将这里的返回值放到对应的key的value中！
//             * @param arg0
//             * @return
//             */
//            @Override
//            public String apply(String arg0) {
//                return "create:" + arg0;
//            }
//
//        });
        // new MapMaker().concurrencyLevel(5).

    }
}
