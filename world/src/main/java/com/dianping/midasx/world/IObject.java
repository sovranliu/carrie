package com.dianping.midasx.world;

import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.world.annotation.Method;
import com.dianping.midasx.world.annotation.Property;
import com.dianping.midasx.world.annotation.Relative;

/**
 * 代理对象接口类
 */
public interface IObject {
    /**
     * 获取属性
     *
     * @return 属性值
     */
    @Property
    public Object property(String property);

    /**
     * 调用方法
     *
     * @param method 方法名称
     * @param args 参数列表
     * @return 方法值
     */
    @Method
    public Object invoke(String method, Object... args);

    /**
     * 获取关系对象
     *
     * @param clazz 关系类
     * @return 关系对象
     */
    @Relative
    public <T> T relative(Class<T> clazz);

    /**
     * 获取关系对象列表
     *
     * @param clazz 关系类
     * @return 关系对象列表
     */
    @Relative
    public <T> ICollection<T> relatives(Class<T> clazz);
}
