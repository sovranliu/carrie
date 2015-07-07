package com.dianping.midasx.base;

import com.dianping.midasx.base.logic.CompareCondition;

/**
 * 条件检查
 */
public class TestCondition {
//    public static class MyCompareCondition extends CompareCondition<Integer, Integer, MyCompareCondition> { }
//
//    public static void main(String[] argv) {
//        // test1();
//        test2();
//    }
//
//    public static void test1() {
//        // A & (B || C)
//        // ? > 1 & (? < 2 || ? == 3)
//        MyCompareCondition a = new MyCompareCondition();
//        a.value = 1;
//        a.compareType = CompareCondition.COMPARETYPE_GREATERTHAN;
//        MyCompareCondition b = new MyCompareCondition();
//        b.value = 2;
//        b.compareType = CompareCondition.COMPARETYPE_LESSTHAN;
//        MyCompareCondition c = new MyCompareCondition();
//        c.value = 3;
//        c.compareType = CompareCondition.COMPARETYPE_EQUAL;
//        //
//        b.put(false, c);
//        a.put(true, b);
//        //
//        System.out.println(a.check(1));
//        System.out.println(a.check(3));
//        System.out.println(a.check(5));
//        //
//        System.out.println(a.toString(0));
//    }
//
//    public static void test2() {
//        // A || (B && C) || (D && E)
//        // ? <= 1 || (? > 2 && ? < 5) || (>= 6 && == 10)
//        MyCompareCondition a = new MyCompareCondition();
//        a.value = 1;
//        a.compareType = CompareCondition.COMPARETYPE_LESSEQUAL;
//        MyCompareCondition b = new MyCompareCondition();
//        b.value = 2;
//        b.compareType = CompareCondition.COMPARETYPE_GREATERTHAN;
//        MyCompareCondition c = new MyCompareCondition();
//        c.value = 5;
//        c.compareType = CompareCondition.COMPARETYPE_LESSTHAN;
//        MyCompareCondition d = new MyCompareCondition();
//        d.value = 6;
//        d.compareType = CompareCondition.COMPARETYPE_GREATERTHAN;
//        MyCompareCondition e = new MyCompareCondition();
//        e.value = 10;
//        e.compareType = CompareCondition.COMPARETYPE_EQUAL;
//        //
//        b.puts(true, c);
//        d.puts(true, e);
//        a.puts(false, b);
//        a.puts(false, d);
//        //
//        System.out.println(a.check(1));
//        System.out.println(a.check(3));
//        System.out.println(a.check(4));
//        System.out.println(a.check(5));
//        System.out.println(a.check(6));
//        //
//        System.out.println(a.toString(0));
//    }
}
