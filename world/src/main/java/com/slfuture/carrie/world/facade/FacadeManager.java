package com.slfuture.carrie.world.facade;

import com.slfuture.carrie.world.invoker.RemoteInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 管理器代理类
 */
public class FacadeManager implements InvocationHandler {
    /**
     * 名称
     */
    public String name = null;


    /**
     * 构造函数
     */
    public FacadeManager() { }

    /**
     * 构造函数
     *
     * @param name 名字
     */
    public FacadeManager(String name) {
        this.name = name;
    }

    /**
     * 构建代理实例
     *
     * @param name 名字
     * @param clazz 类对象
     * @return 代理实例
     */
    public static <T> T build(String name, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new FacadeManager(name));
    }

    /**
     * Processes a method invocation on a proxy instance and returns
     * the result.  This method will be invoked on an invocation handler
     * when a method is invoked on a proxy instance that it is
     * associated with.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return RemoteInvoker.instance().call(name, method.getName(), args);
    }
}
