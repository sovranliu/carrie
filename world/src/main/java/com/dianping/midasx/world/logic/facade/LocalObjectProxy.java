package com.dianping.midasx.world.logic.facade;

import com.dianping.midasx.base.type.Record;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.world.logic.facade.core.IObjectProxy;

/**
 * 本地代理对象类
 */
public class LocalObjectProxy implements IObjectProxy {
    /**
     * 簇名称
     */
    public String cluster;
    /**
     * 实体对象
     */
    public Object target = null;


    /**
     * 构造函数
     */
    public LocalObjectProxy() { }

    /**
     * 构造函数
     *
     * @param cluster 簇名称
     * @param target 实体对象
     */
    public LocalObjectProxy(String cluster, Object target) {
        this.cluster = cluster;
        this.target = target;
    }

    /**
     * 获知指定名称的属性
     *
     * @param name 属性名
     * @return 属性值
     */
    @Override
    public Object property(String name) {
        return null;
    }

    /**
     * 获知指定名称的关系对象
     *
     * @param clazz 关系对象类
     * @param name  关系名
     * @return 关系对象
     */
    @Override
    public <T> T relative(Class<T> clazz, String name) {
        return null;
    }

    /**
     * 获知指定名称的关系对象集
     *
     * @param clazz 关系对象类
     * @param name  关系名
     * @return 关系对象集
     */
    @Override
    public <T> ICollection<T> relatives(Class<T> clazz, String name) {
        return null;
    }

    /**
     * 调用指定方法名的方法
     *
     * @param name       属性名
     * @param classes    参数类列表
     * @param parameters 参数列表
     * @return 调用结果值
     */
    @Override
    public Object invoke(String name, Class<?>[] classes, Object[] parameters) {
        return null;
    }
}
