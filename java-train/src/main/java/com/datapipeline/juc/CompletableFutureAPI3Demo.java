package com.datapipeline.juc;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureAPI3Demo {
    public static void main(String[] args) {
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {}).join());
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenAccept(r -> System.out.println(r)).join());
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenApply(r -> r + "resultB").join());
    }
}
