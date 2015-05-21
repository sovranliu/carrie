package com.slfuture.carrie.base.type.core;

import com.slfuture.carrie.base.logic.core.ICondition;

/**
 * 带标记图接口
 */
public interface ISignedDigraph<K, T extends ISignedDigraph<K, T>> extends ITable<K, T> {
    /**
     * 按条件搜索
     *
     * @param condition 条件对象
     * @return 查找路径，搜索不到返回null
     */
    public IList<T> search(ICondition<T> condition);

    /**
     * 按条件查找全部
     *
     * @param condition 条件对象
     * @return 查找结果，搜索不到返回空集合
     */
    public ISet<T> find(ICondition<T> condition);

    /**
     * 按条件查找一个
     *
     * @param condition 条件对象
     * @return 查找结果，搜索不到返回null
     */
    public T load(ICondition<T> condition);
}
