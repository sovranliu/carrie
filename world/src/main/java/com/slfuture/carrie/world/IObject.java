package com.slfuture.carrie.world;

import com.slfuture.carrie.world.annotation.Method;
import com.slfuture.carrie.world.annotation.Property;
import com.slfuture.carrie.world.annotation.Relative;

/**
 * 代理对象接口类
 */
public interface IObject {
    /**
     * 获取簇名称
     */
    public String cluster();

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
     * @param name 关系名称
     * @param clazz 关系类
     * @return 关系对象
     */
    @Relative
    public <T> T relative(String name, Class<T> clazz);

    /**
     * 获取关系对象列表
     *
     * @param name 关系名称
     * @param clazz 关系类
     * @return 关系对象列表
     */
    @Relative
    public <T> T[] relatives(String name, Class<T> clazz);

    /**
     * 获取关系对象
     *
     * @param name 关系名称
     * @return 关系对象
     */
    @Relative
    public Object relative(String name);
}
