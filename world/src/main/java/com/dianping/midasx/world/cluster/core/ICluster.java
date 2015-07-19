package com.dianping.midasx.world.cluster.core;

import com.dianping.midasx.base.model.Method;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.ITable;
import com.dianping.midasx.world.relation.Condition;
import com.dianping.midasx.world.relation.Relation;

/**
 * 簇接口
 */
public interface ICluster<T> extends ITable<String, Relation> {
    /**
     * 查找符合条件的对象
     *
     * @param condition 查找条件
     * @return 查找到的指定对象
     */
    public T find(Condition condition);

    /**
     * 查找符合条件的对象集
     *
     * @param condition 查找条件
     * @return 查找到的对象集
     */
    public ICollection<T> finds(Condition condition);

    /**
     * 调用方法
     *
     * @param id 被调用对象标志符
     * @param method 方法
     * @param args 参数列表
     * @return 调用结果
     */
    public Object invoke(Object id, Method method, Object...args);
}
