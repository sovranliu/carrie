package com.dianping.midasx.base.logic.grammar;

/**
 * 符号语法
 */
public class SymbolLogicalGrammar extends LogicalGrammar {
    /**
     * 与
     */
    @Override
    public String and() {
        return "&&";
    }

    /**
     * 或
     */
    @Override
    public String or() {
        return "||";
    }

    /**
     * 非
     */
    @Override
    public String not() {
        return "!";
    }
}
