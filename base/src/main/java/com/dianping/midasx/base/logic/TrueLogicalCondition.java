package com.dianping.midasx.base.logic;

/**
 * 真逻辑条件类
 */
public class TrueLogicalCondition<T> extends LogicalCondition<T, TrueLogicalCondition<T>> {
    /**
     * 检查
     *
     * @param target 检查目标
     * @return 目标是否通过检查
     */
    @Override
    public boolean onCheck(T target) {
        return true;
    }
}
