package com.dianping.midasx.world.facade;

import com.dianping.midasx.world.IObject;
import com.dianping.midasx.world.invoker.RemoteInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 世界对象代理类
 */
public class FacadeObject implements InvocationHandler {
    /**
     * 名称
     */
    public String name = null;


    /**
     * 构造函数
     */
    public FacadeObject() { }

    /**
     * 构造函数
     *
     * @param name 名字
     */
    public FacadeObject(String name) {
        this.name = name;
    }

    /**
     * 获取指定名称的属性值
     *
     * @param target 目标对象
     * @param name 属性名
     * @return 属性值
     */
    public static <T> T field(IObject target, String name) {
        // TODO:
        return null;
    }

    /**
     * 方法调用
     *
     * @param target 目标对象
     * @param method 方法名
     * @param parameters 参数列表
     * @return 返回值
     */
    public static Object call(IObject target, String method, Object... parameters) throws Exception {
        // TODO:
        return null;
    }

    /**
     * 构建代理实例
     *
     * @param name 名字
     * @param clazz 类对象
     * @return 代理实例
     */
    public static <T> T build(String name, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new FacadeObject(name));
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
