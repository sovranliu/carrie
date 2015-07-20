package com.dianping.midasx.world.cluster.core;

import com.dianping.midasx.base.model.Method;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.ITable;
import com.dianping.midasx.world.logic.Agent;
import com.dianping.midasx.world.relation.Condition;
import com.dianping.midasx.world.relation.Relation;

/**
 * 簇接口
 */
public interface ICluster<T> extends ITable<String, Relation> {
    /**
     * 获取簇名称
     *
     * @return 簇名称
     */
    public String name();

    /**
     * 获取主键
     *
     * @return 主键
     */
    public String primaryKey();

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
     * 查找符合条件的代理
     *
     * @param condition 查找条件
     * @return 查找到的指定代理
     */
    public Agent findAgent(Condition condition);

    /**
     * 查找符合条件的代理集
     *
     * @param condition 查找条件
     * @return 查找到的代理集
     */
    public ICollection<Agent> findAgents(Condition condition);

    /**
     * 调用方法
     *
     * @param target 被调用对象
     * @param method 方法
     * @param args 参数列表
     * @return 调用结果
     */
    public Object invoke(T target, Method method, Object...args);
}
