package com.slfuture.carrie.base;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Test {
    public final static String T = "hello";

    public static int indexOf(String str, String subStr) {
        char[] arr = str.toCharArray();
        char[] subArr = subStr.toCharArray();
        for(int i = 0; i < arr.length; i++) {
            if(i + subArr.length > arr.length) {
                return -1;
            }
            boolean sentry = true;
            for(int j = 0; j < subArr.length; j++) {
                if(arr[i + j] != subArr[j]) {
                    sentry = false;
                    break;
                }
            }
            if(sentry) {
                return i;
            }
        }
        return -1;
    }

    public static void do1() throws IOException {
        throw new IOException("IO SSS");
    }

    public static void do2() {
        try {
            do1();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void do3() {
        try {
            do2();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void c(Object object) {
        System.out.println("c(Object)");
    }

    public static void c(String object) {
        System.out.println("c(String)");
    }


    public static class C1 {
        public int p1;
    }

    public static class C2 extends C1 {
        public int p2;
    }

    public static void main(String[] argv) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, Exception {
        String sssss = "柳君";
        System.out.println(sssss);

        HashMap<String, String> s1 = new HashMap<String, String>();
        s1.put("1", "2");
        s1.put("2", "3");
        HashMap<String, String> s2 = new HashMap<String, String>();
        s2.put("1", "2");
        s2.put("2", "3");
        System.out.println(s1.equals(s2));
        s2.put("2", "3");
        System.out.println(s1.equals(s2));
        s2.put("3", "4");
        System.out.println(s1.equals(s2));

        if(s1.getClass().isAssignableFrom(HashMap.class)) {

        }


        Field[] fields = C2.class.getFields();
        System.out.println(fields);



        System.out.println(UUID.randomUUID());

        String s = "123";
        Object ooo = s;
        c(ooo);

        do3();

        Integer i = 1;
        Object o = i;
        Integer ii = 2;
        Object oo = i;

        Comparable<?> c = (Comparable<?>) i;
        Method methodx = o.getClass().getDeclaredMethod("compareTo", o.getClass());
        System.out.println(methodx.invoke(o, oo));
        // System.out.println(c.compareTo(oo));

        String aa = "hello";
        StringBuilder sb = new StringBuilder();
        sb.append("hello1");
        sb.delete(5, 6);
        System.out.println(aa == T);
        System.out.println(aa == sb.toString());

        // 按指定模式在字符串查找
        String line = "abc123efg";
        String pattern = "(\\d+)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find()) {
            System.out.println("Found value: " + m.start() + "," + m.end() );
        } else {
            System.out.println("NO MATCH");
        }



        byte[] bs = new byte[4];
        //\xF0\x9F\x98\x8A
        bs[0] = (byte)0xF0;
        bs[1] = (byte)0x9F;
        bs[2] = (byte)0x98;
        bs[3] = (byte)0x8A;
        String a = new String(bs, "utf-8");
        System.out.println(a);
        bs[3] = (byte)0xF0;
        bs[2] = (byte)0x9F;
        bs[1] = (byte)0x98;
        bs[0] = (byte)0x8A;
        a = new String(bs, "ISO-8859-1");
        System.out.println(a);
        bs[0] = (byte)0x0F;
        bs[1] = (byte)0xF9;
        bs[2] = (byte)0x89;
        bs[3] = (byte)0xA8;
        a = new String(bs, "ISO-8859-1");
        System.out.println(a);
        bs[3] = (byte)0x0F;
        bs[2] = (byte)0xF9;
        bs[1] = (byte)0x89;
        bs[0] = (byte)0xA8;
        a = new String(bs, "ISO-8859-1");
        System.out.println(a);
        System.out.println(indexOf("12345678", "567"));

        TestClass c1 = new TestClass();
        TestClass2 c2 = new TestClass2();
        Class clazz = c1.getClass();
        Method method = clazz.getDeclaredMethod("method");
        Method method2 = TestClass2.class.getDeclaredMethod("method");
        if(method == method2) {
            System.out.println("method equal");
        }
        method.invoke(c2);


        if(c1 instanceof ITestInterface) {
            System.out.println("c1 instanceof ITestInterface:True");
        }
        else {
            System.out.println("c1 instanceof ITestInterface:False");
        }
        if(c2 instanceof ITestInterface) {
            System.out.println("c2 instanceof ITestInterface:True");
        }
        else {
            System.out.println("c2 instanceof ITestInterface:False");
        }
        if(c1 instanceof ITestInterface2) {
            System.out.println("c1 instanceof ITestInterface2:True");
        }
        else {
            System.out.println("c1 instanceof ITestInterface2:False");
        }
        if(c2 instanceof ITestInterface2) {
            System.out.println("c2 instanceof ITestInterface2:True");
        }
        else {
            System.out.println("c2 instanceof ITestInterface2:False");
        }
    }
}
