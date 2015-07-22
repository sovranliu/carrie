package com.slfuture.carrie.base.logic.grammar;

import com.slfuture.carrie.base.logic.core.ILogicalGrammar;

/**
 * 逻辑语法抽象类
 */
public abstract class LogicalGrammar implements ILogicalGrammar {
    /**
     * 转换为字符串
     *
     * @param grammar 语法类型
     * @return 字符串
     */
    @Override
    public String toString(int grammar) {
        switch(grammar) {
            case ILogicalGrammar.GRAMMAR_AND:
                return and();
            case ILogicalGrammar.GRAMMAR_OR:
                return or();
            case ILogicalGrammar.GRAMMAR_NOT:
                return not();
            default:
                return null;
        }
    }
}
