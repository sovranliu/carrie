package com.slfuture.carrie.base.logic.core;

/**
 * 条件接口
 */
public interface ICondition<T> {
    /**
     * 检查
     *
     * @param target 检查目标
     * @return 目标是否通过检查
     */
    public boolean check(T target);
}
