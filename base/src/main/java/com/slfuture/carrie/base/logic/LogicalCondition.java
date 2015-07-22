package com.slfuture.carrie.base.logic;

import com.slfuture.carrie.base.logic.core.ICondition;
import com.slfuture.carrie.base.logic.core.IState;

/**
 * 逻辑校验类
 *
 * T 对象类型， S 子类
 */
public abstract class LogicalCondition<T, S extends LogicalCondition<T, S>> extends BooleanRouteDigraph<T, S> implements ICondition<T>, IState<Boolean, S> {
    /**
     * 检查
     *
     * @param target 检查目标
     * @return 目标是否通过检查
     */
    @Override
    public boolean check(T target) {
        return route(target);
    }

    /**
     * 检查回调
     *
     * @param target 检查目标
     * @return 目标是否通过检查
     */
    public abstract boolean onCheck(T target);

    /**
     * 反转
     *
     * @param value 值
     * @return 输入
     */
    @Override
    public Boolean onRoute(T value) {
        return onCheck(value);
    }
}
