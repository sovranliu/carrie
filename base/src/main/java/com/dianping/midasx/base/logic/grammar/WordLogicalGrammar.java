package com.dianping.midasx.base.logic.grammar;

/**
 * 文字语法
 */
public class WordLogicalGrammar extends LogicalGrammar {
    /**
     * 与
     */
    @Override
    public String and() {
        return "AND";
    }

    /**
     * 或
     */
    @Override
    public String or() {
        return "OR";
    }

    /**
     * 非
     */
    @Override
    public String not() {
        return "NOT";
    }
}
