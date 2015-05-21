package com.slfuture.carrie.utility.rpc.core;

import com.slfuture.carrie.base.logic.core.ICondition;

/**
 * 对象持有接口
 */
public interface IEntityManager<T> {
    /**
     * 加载符合条件的对象
     *
     * @param condition 条件
     * @return 符合条件的对象
     */
    public T load(ICondition<T> condition);
}
