package com.datapipeline.basis;

import java.nio.file.AccessDeniedException;
import java.util.Locale;

/**
 * 这个类主要测试方法引用的相关功能
 * 从JDK1.8开始，可以使用方法引用。
 *
 * 方法引用的操作符是双冒号"::"。
 *
 * 方法引用可以认为是Lambda表达式的一种特殊形式，Lambda表达式可以让开发者自定义抽象方法的实现代码，
 * 方法引用则可以让开发者直接引用已存在的实现方法，作为Lambda表达式的Lambda体(参数列表得一致)。
 *
 * 方法引用有几种形式：
 * 一，类::静态方法
 * 二，对象::实例方法
 * 三，类::实例方法
 * 四，构造器引用，Class::new
 * 五，数组引用，数组::new
 */
public class MethodReference {
    public static void main(String[] args) {
        // 先写一个简单的lambda表达式
        ImTheOne imTheOne = ((a, b) -> a + b);
        String s = imTheOne.handleString("one", "two");
        System.out.println("一个简单的lamdba表达式示例====================="+s);
        // 需要两点需要注意： 1：OneClass类中的方法必须为静态方法 2：concatString参数列表必须和接口中的参数列表一致
        ImTheOne imTheOne1 = OneClass::concatString;
        String s1 = imTheOne1.handleString("111", "2222");
        System.out.println("测试类::静态方法========="+s1);
        OneClass oneClass = new OneClass();
        ImTheOne imTheOne2 = (a,b)->oneClass.sum(a,b);
        String s2 = imTheOne2.handleString("abc", "def");
        System.out.println("测试对象::实例方法==============="+s2);

        ImThe imThe = TargetClass::new;
        TargetClass target = imThe.getTargetClass("abc");
        System.out.println("测试构造器引用："+target.oneString);

        ImThe imThe1 = a -> new TargetClass("abc");
        TargetClass targetClass = imThe1.getTargetClass("123");  // 注意，我们在调用这个getTargetClass方法时实际上在调用上面的构造方法，所以这个位置输出的是abc,而不是123
        System.out.println(targetClass.oneString);

    }

    @FunctionalInterface
    public interface ImThe {
        TargetClass getTargetClass(String a);
    }

    @FunctionalInterface
    public interface ImTheOne {
        String handleString(String a, String b);
    }
}

// 测试 类::方法引用
class OneClass {
    public static String concatString(String a,String b) {
        return a+b;
    }

    public String sum(String a,String b){
        return a.toLowerCase(Locale.ROOT)+b.toUpperCase();
    }
}


class TargetClass {
    String oneString;

    public TargetClass() {
        oneString = "default";
    }

    public TargetClass(String a) {
        oneString = a;
    }
}