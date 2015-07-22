package com.slfuture.carrie.world;

import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.world.annotation.Method;
import com.slfuture.carrie.world.annotation.Property;
import com.slfuture.carrie.world.annotation.Relative;

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
     * @param method 方法
     * @param args 参数列表
     * @return 方法值
     */
    @Method
    public Object invoke(com.slfuture.carrie.base.model.Method method, Object... args);

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
