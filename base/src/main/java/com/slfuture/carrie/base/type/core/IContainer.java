package com.slfuture.carrie.base.type.core;

import com.slfuture.carrie.base.logic.core.ICondition;

/**
 * 容器接口
 */
public interface IContainer<T> {
    /**
     * 按条件查找一个
     *
     * @param condition 条件对象
     * @return 查找结果，搜索不到返回null
     */
    public T load(ICondition<T> condition);

    /**
     * 条件查找
     *
     * @param condition 条件对象
     * @return 查找到的对象或对象集
     */
    public ISet<T> find(ICondition<T> condition);
}
