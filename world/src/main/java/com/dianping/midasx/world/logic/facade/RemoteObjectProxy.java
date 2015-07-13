package com.dianping.midasx.world.logic.facade;

import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.IMapping;
import com.dianping.midasx.world.logic.facade.core.IObjectProxy;

/**
 * 远程代理对象类
 */
public class RemoteObjectProxy implements IObjectProxy {
    /**
     * 簇名称
     */
    public String cluster;
    /**
     * 属性映射
     */
    public IMapping<String, Object> properties = null;


    /**
     * 构造函数
     */
    public RemoteObjectProxy() { }

    /**
     * 构造函数
     *
     * @param cluster 簇名称
     * @param properties 属性映射
     */
    public RemoteObjectProxy(String cluster,  IMapping<String, Object> properties) {
        this.cluster = cluster;
        this.properties = properties;
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
