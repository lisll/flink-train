package com.datapipeline.basis;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Java通过反射可以在运行时获取类型信息，但其有个缺点就是执行速度较慢。于是从Java 7开始提供了另一套API MethodHandle 。
 * 其与反射的作用类似，可以在运行时访问类型信息，但是据说其执行效率比反射更高，也被称为Java的 现代化反射。
 *
 * 一些重要的类
 *  1,MethodHandle
 * 它是可对直接执行的方法（或域、构造方法等）的类型的引用，或者说，它是一个有能力安全调用方法的对象。
 * 换个方法来说，通过句柄我们可以直接调用该句柄所引用的底层方法。
 * 从作用上来看，方法句柄类似于反射中的Method类，但是方法句柄的功能更加强大、使用更加灵活、性能也更好。
 *
 *  2,MethodType
 *  它是表示方法签名类型的不可变对象。每个方法句柄都有一个MethodType实例，用来指明方法的返回类型和参数类型。
 *  它的类型完全由参数类型和方法类型来确定，而与它所引用的底层的方法的名称和所在的类没有关系。
 *  举个例子，例如String类的length方法和Integer类的intValue方法的方法句柄的类型就是一样的，
 *  因为这两个方法都没有参数，而且返回值类型都是int，
 *  则我们可以通过下列语句获取同一个方法类型：MethodType mt = MethodType.methodType(int.class);
 *  MethodType的对象实例只能通过MethodType类中的静态工厂方法来创建，而且MethodType类的所有对象实例都是不可变的，类似于String类。
 *  如果修改了MethodType实例中的信息，就会生成另外一个MethodType实例。
 *
 *  3,Lookup
 *  MethodHandle 的创建工厂，通过它可以创建MethodHandle，值得注意的是检查工作是在创建时处理的，而不是在调用时处理。
 */
public class MethodHandlesDemo {
    private static final Map<String, Map<MethodType, MethodHandle>> METHOD_CACHE = new ConcurrentHashMap<>();
    private static final String CLASS_CONSTRUCTOR = "CONSTRUCTOR";
    private String name;
    public static void main(String[] args) throws Throwable {

        Class<MethodHandlesDemo> clazz = MethodHandlesDemo.class;
        MethodHandlesDemo methodHandlesDemo = clazz.getConstructor().newInstance();
//        MethodHandle virtualHandle = ReflectUtils.getMethodHandle(Void.class, MethodHandlesDemo.class, "demo",String.class);
//        MethodHandle handle= ReflectUtils.getVirtual(String.class,clazz,"test1",int.class,String.class);
        /**
         * 注意： 测试之前，必须要有空参构造方法，因为对象先要实例化
         * 否则会报错：java.lang.NoSuchMethodException com.datapipeline.basis.MethodHandlesDemo.<init>()
         */
        // 测试获取普通方法句柄
        MethodHandle virtualHandle = ReflectUtils.getVirtual(String.class,clazz,"test1",int.class,String.class);
        virtualHandle.bindTo(methodHandlesDemo).invoke(22,"bb");
        // 测试获取静态方法的方法句柄
        MethodHandle staticHandle = ReflectUtils.getStatic(String.class, clazz, "test2", int.class, String.class);
        staticHandle.invoke(33,"mm");
//        staticHandle.bindTo(methodHandlesDemo).invoke(33,"oo");   // 调用静态方法时不能这么写，否则会报错
        // 测试获取构造方法的方法句柄   注意： 测试有参构造方法时，必须得有空参构造方法
        MethodHandle constructMethodHandle = ReflectUtils.getConstructMethodHandle(MethodHandlesDemo.class,String.class);
        constructMethodHandle.invoke("mmmm-mm");



//        constructMethodHandle.invoke("aa");
//        handle.invokeExact(new MethodHandlesDemo(),11,"aa");
//        System.out.println(virtualHandle.bindTo(methodHandlesDemo).invoke(1,"method"));
//        virtualHandle.invokeExact(new MethodHandlesDemo(), "张三");
//        virtualHandle.bindTo(methodHandlesDemo).invoke("zhangsan");

//        System.out.println(virtualHandle.invokeExact(new MethodHandlesDemo(),"mt"));


//        MethodHandle staticHandle = ReflectUtils.getStatic(String.class, clazz, "test2", int.class, String.class);
//        System.out.println(staticHandle.invoke(2,"method"));

    }




//    // 测试构造方法
//    public MethodHandlesDemo(String name){
//        System.out.println("构造方法执行->"+name);
//    }


    // 当存在有参构造时，这个空参构造是一定需要有的，如果不存在有参构造，则不用显式的写出来，因为默认就会有。
    public MethodHandlesDemo() {
        System.out.println("空参构造执行");
    }

    public MethodHandlesDemo(String name){
        System.out.println("有参构造方法执行"+name);
    }

    // 测试普通方法
    public String test1(int a, String b) {
        System.out.println("test1 -> " + a + b);
        return a + b;
    }
    // 测试静态方法
    public static String  test2(int a, String b) {
        System.out.println("test2 -> " + a + b);
        return a + b;
    }


    public static MethodHandle getMethod(String methodName,Class clazz,MethodType methodType){
        try {
            return CLASS_CONSTRUCTOR.equals(methodName)
                    ? MethodHandles.publicLookup().findConstructor(clazz, methodType)
                    : MethodHandles.publicLookup().findVirtual(clazz, methodName, methodType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
