package com.dianping.midasx.world.logic.facade.core;

import com.dianping.midasx.base.type.core.ICollection;

/**
 * 对象代理接口
 */
public interface IObjectProxy {
    /**
     * 获知指定名称的属性
     *
     * @param name 属性名
     * @return 属性值
     */
    public Object property(String name);

    /**
     * 获知指定名称的关系对象
     *
     * @param clazz 关系对象类
     * @param name 关系名
     * @return 关系对象
     */
    public <T> T relative(Class<T> clazz, String name);

    /**
     * 获知指定名称的关系对象集
     *
     * @param clazz 关系对象类
     * @param name 关系名
     * @return 关系对象集
     */
    public <T> ICollection<T> relatives(Class<T> clazz, String name);

    /**
     * 调用指定方法名的方法
     *
     * @param name 属性名
     * @param classes 参数类列表
     * @param parameters 参数列表
     * @return 调用结果值
     */
    public Object invoke(String name, Class<?>[] classes, Object[] parameters);
}
