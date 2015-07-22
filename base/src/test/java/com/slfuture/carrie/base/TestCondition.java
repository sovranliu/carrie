package com.slfuture.carrie.base;

import com.slfuture.carrie.base.logic.CompareCondition;
import com.slfuture.carrie.base.logic.ComparisonTool;
import com.slfuture.carrie.base.logic.grammar.SymbolLogicalGrammar;

/**
 * 条件检查
 */
public class TestCondition {
    public static class MyCompareCondition extends CompareCondition<Integer, MyCompareCondition> { }

    public static void main(String[] argv) {
        // test1();
        test2();
    }

    public static void test1() {
        // A & (B || C)
        // ? > 1 & (? < 2 || ? == 3)
        MyCompareCondition a = new MyCompareCondition();
        a.target = (1);
        a.compareType = ComparisonTool.COMPARETYPE_GREATERTHAN;
        MyCompareCondition b = new MyCompareCondition();
        b.target = (2);
        b.compareType = ComparisonTool.COMPARETYPE_LESSTHAN;
        MyCompareCondition c = new MyCompareCondition();
        c.target = (3);
        c.compareType = ComparisonTool.COMPARETYPE_EQUAL;
        //
        b.put(false, c);
        a.put(true, b);
        //
        System.out.println(a.check(1));
        System.out.println(a.check(3));
        System.out.println(a.check(5));
        //
        System.out.println(a.toString(new SymbolLogicalGrammar()));
    }

    public static void test2() {
        // A || (B && C) || (D && E)
        // ? <= 1 || (? > 2 && ? < 5) || (>= 6 && == 10)
        MyCompareCondition a = new MyCompareCondition();
        a.target = (1);
        a.compareType = ComparisonTool.COMPARETYPE_LESSEQUAL;
        MyCompareCondition b = new MyCompareCondition();
        b.target = (2);
        b.compareType = ComparisonTool.COMPARETYPE_GREATERTHAN;
        MyCompareCondition c = new MyCompareCondition();
        c.target = (5);
        c.compareType = ComparisonTool.COMPARETYPE_LESSTHAN;
        MyCompareCondition d = new MyCompareCondition();
        d.target = (6);
        d.compareType = ComparisonTool.COMPARETYPE_GREATERTHAN;
        MyCompareCondition e = new MyCompareCondition();
        e.target = (10);
        e.compareType = ComparisonTool.COMPARETYPE_EQUAL;
        //
        b.puts(true, c);
        d.puts(true, e);
        a.puts(false, b);
        a.puts(false, d);
        //
        System.out.println(a.check(1));
        System.out.println(a.check(3));
        System.out.println(a.check(4));
        System.out.println(a.check(5));
        System.out.println(a.check(6));
        //
        System.out.println(a.toString(new SymbolLogicalGrammar()));
    }
}
