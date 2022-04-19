package com.datapipeline.basis;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class ReflectUtils {

    // 获取普通方法的句柄
    public static MethodHandle getVirtual(Class retrunClass,Class clazz ,String methodName,Class ... parameter) throws NoSuchMethodException, IllegalAccessException {
        MethodType methodType = MethodType.methodType(retrunClass, parameter);
        return MethodHandles.publicLookup().findVirtual(clazz,methodName,methodType);
    }

    // 获取静态方法的句柄
    public static MethodHandle getStatic(Class returnClass,Class clazz,String methodName,Class ... parameter) throws NoSuchMethodException, IllegalAccessException {
        MethodType methodType = MethodType.methodType(returnClass, parameter);
        return MethodHandles.publicLookup().findStatic(clazz,methodName,methodType);
    }

    // 获取构造方法的句柄
    public static MethodHandle getConstructMethodHandle(Class clazz ,Class ... parameter) throws NoSuchMethodException, IllegalAccessException {
        MethodType methodType = MethodType.methodType(void.class, parameter);
        return MethodHandles.publicLookup().findConstructor(clazz,methodType);
    }

}
