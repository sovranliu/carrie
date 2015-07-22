package com.slfuture.carrie.base.logic.grammar;

/**
 * 符号语法
 */
public class SymbolLogicalGrammar extends LogicalGrammar {
    /**
     * 语法常量
     */
    public final static String GRAMMAR_AND = "&&";
    public final static String GRAMMAR_OR = "||";
    public final static String GRAMMAR_NOT = "!";


    /**
     * 与
     */
    @Override
    public String and() {
        return GRAMMAR_AND;
    }

    /**
     * 或
     */
    @Override
    public String or() {
        return GRAMMAR_OR;
    }

    /**
     * 非
     */
    @Override
    public String not() {
        return GRAMMAR_NOT;
    }
}
