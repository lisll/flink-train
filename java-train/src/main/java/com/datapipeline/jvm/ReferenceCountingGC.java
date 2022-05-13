package com.datapipeline.jvm;

public class ReferenceCountingGC {
    public Object instance = null;
    private static final int _1MB = 1024*1024;
    private byte[] bigsize = new byte[2*_1MB];

    public static void main(String[] args) {
        testGc();
    }
    public static void testGc(){
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();
        objA.instance = objB;
        objB.instance = objA;

        System.gc();
    }
}
