package com.slfuture.carrie.base.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 代理类
 */
public class Delegate<P, R> {
    /**
     * 对象
     */
    public Object object;
    /**
     * 方法
     */
    public Method method;


    /**
     * 构造函数
     */
    public Delegate() {}

    /**
     * 构造函数
     *
     * @param object 对象
     * @param method 方法对象
     */
    public Delegate(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    /**
     * 调用方法
     *
     * @param parameters 参数列表
     * @return 方法返回值
     */
    public R invoke(P... parameters) throws InvocationTargetException, IllegalAccessException {
        return (R) method.invoke(object, parameters);
    }
}
