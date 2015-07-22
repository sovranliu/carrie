package com.slfuture.carrie.base.logic.core;

/**
 * 逻辑语法接口
 */
public interface ILogicalGrammar {
    /**
     * 未定义标识
     */
    public final static int GRAMMAR_UNDEFINED = 0;
    /**
     * 与标识
     */
    public final static int GRAMMAR_AND = 1;
    /**
     * 或标识
     */
    public final static int GRAMMAR_OR = 2;
    /**
     * 否标识
     */
    public final static int GRAMMAR_NOT = 3;


    /**
     * 与
     */
    public String and();
    /**
     * 或
     */
    public String or();
    /**
     * 非
     */
    public String not();
    /**
     * 转换为字符串
     *
     * @param grammar 语法类型
     * @return 字符串
     */
    public String toString(int grammar);
}
