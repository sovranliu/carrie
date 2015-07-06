package com.dianping.midasx.world.entity.remote;

import com.dianping.midasx.base.type.Map;
import com.dianping.midasx.world.invoker.RemoteInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 远程对象类
 */
public class RemoteObject implements InvocationHandler {
    /**
     * 对象名称
     */
    public String name;
    /**
     * 属性缓存
     */
    public Map<String, Object> cached = new Map<String, Object>();


    /**
     * Processes a method invocation on a proxy instance and returns
     * the result.  This method will be invoked on an invocation handler
     * when a method is invoked on a proxy instance that it is
     * associated with.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = cached.get(method.getName());
        if(null != result) {
            return result;
        }
        return RemoteInvoker.instance().call(name, method.getName(), args);
    }
}
