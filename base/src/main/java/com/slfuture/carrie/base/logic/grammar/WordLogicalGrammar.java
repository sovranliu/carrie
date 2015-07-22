package com.slfuture.carrie.base.logic.grammar;

/**
 * 文字语法
 */
public class WordLogicalGrammar extends LogicalGrammar {
    /**
     * 语法常量
     */
    public final static String GRAMMAR_AND = "and";
    public final static String GRAMMAR_OR = "or";
    public final static String GRAMMAR_NOT = "not";


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
