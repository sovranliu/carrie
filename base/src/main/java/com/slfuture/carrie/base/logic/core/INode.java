package com.slfuture.carrie.base.logic.core;

import com.slfuture.carrie.base.type.core.ISet;

/**
 * 节点接口
 */
public interface INode<T extends INode<T>> {
    /**
     * 获取单个邻居
     *
     * @param condition 节点查询条件
     * @return 单个邻居
     */
    public T neighbor(ICondition<T> condition);

    /**
     * 获取多个邻居
     *
     * @param condition 节点查询条件
     * @return 多个邻居
     */
    public ISet<T> neighbors(ICondition<T> condition);
}
