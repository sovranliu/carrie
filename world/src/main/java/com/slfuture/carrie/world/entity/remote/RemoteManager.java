package com.slfuture.carrie.world.entity.remote;

import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.world.IManager;
import com.slfuture.carrie.world.invoker.RemoteInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 远程管理器类
 */
public class RemoteManager implements InvocationHandler {
    /**
     * 对象名称
     */
    public String name;


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

    /**
     * 构建远程管理器
     *
     * @param cluster 簇
     * @param conf 配置对象
     * @return 远程管理器
     */
    public static IManager<?> build(RemoteCluster cluster, IXMLNode conf) {
        String classString = conf.get("class");
        Class<?> clazz = null;
        try {
            clazz = Class.forName(classString);
        }
        catch(ClassNotFoundException ex) {
            return null;
        }
        RemoteManager manager = new RemoteManager();
        manager.name = conf.get("name");
        if(null == manager.name) {
            manager.name = cluster.name + ".Manager";
        }
        Class<?>[] interfaces = new Class<?>[1];
        interfaces[0] = clazz;
        return (IManager<?>) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, manager);
    }
}
