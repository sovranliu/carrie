package com.dianping.midasx.base.type;

import com.dianping.midasx.base.type.core.ILink;
import com.dianping.midasx.base.type.core.ISet;
import com.dianping.midasx.base.type.core.ISignedTree;
import com.dianping.midasx.base.type.core.ITable;

/**
 * 标记树类
 */
public class SignedTree<K, T extends SignedTree<K, T>> implements ISignedTree<K, T> {
    /**
     * 父亲节点
     */
    protected T parent = null;
    /**
     * 儿子节点集
     */
    protected ITable<K, T> children = null;


    /**
     * 获取父节点
     *
     * @return 父节点对象
     */
    @Override
    public T parent() {
        return parent;
    }

    /**
     * 设置父节点
     *
     * @param parent 待设置为父节点的节点对象
     * @return 原父节点，若原父节点不存在则返回null
     */
    @Override
    public T setParent(T parent) {
        T result = this.parent;
        this.parent = parent;
        return result;
    }

    /**
     * 获取子节点集合
     *
     * @return 子节点集合
     */
    @Override
    public ITable<K, T> children() {
        return children;
    }

    /**
     * 初始化
     */
    public void onNew() {
        children = new Table<K, T>();
    }
}
