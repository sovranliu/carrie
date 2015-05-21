package com.slfuture.carrie.base.logic;

import com.slfuture.carrie.base.logic.core.ICondition;

/**
 * 真条件类
 */
public class TrueCondition<T> implements ICondition<T> {
    /**
     * 检查
     *
     * @param target 检查目标
     * @return 目标是否通过检查
     */
    @Override
    public boolean check(T target) {
        return true;
    }
}
