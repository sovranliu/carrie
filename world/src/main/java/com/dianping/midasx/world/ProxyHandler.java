package com.dianping.midasx.world;

import com.dianping.midasx.base.model.Identity;
import com.dianping.midasx.base.type.core.IMapping;
import com.dianping.midasx.world.annotation.Property;
import com.dianping.midasx.world.relation.Condition;
import com.dianping.midasx.world.relation.Relation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理句柄类
 */
public class ProxyHandler implements InvocationHandler {
    /**
     * 簇对象
     */
    public Cluster cluster = null;
    /**
     * 属性映射
     */
    private IMapping<String, Object> properties = null;


    /**
     * 构造函数
     */
    public ProxyHandler() { }

    /**
     * 构造函数
     *
     * @param cluster 簇对象
     * @param properties 属性
     */
    public ProxyHandler(Cluster cluster, IMapping<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * Processes a method invocation on a proxy instance and returns
     * the result.  This method will be invoked on an invocation handler
     * when a method is invoked on a proxy instance that it is
     * associated with.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the {@code Method} instance corresponding to
     *               the interface method invoked on the proxy instance.  The declaring
     *               class of the {@code Method} object will be the interface that
     *               the method was declared in, which may be a superinterface of the
     *               proxy interface that the proxy class inherits the method through.
     * @param args   an array of objects containing the values of the
     *               arguments passed in the method invocation on the proxy instance,
     *               or {@code null} if interface method takes no arguments.
     *               Arguments of primitive types are wrapped in instances of the
     *               appropriate primitive wrapper class, such as
     *               {@code java.lang.Integer} or {@code java.lang.Boolean}.
     * @return the value to return from the method invocation on the
     * proxy instance.  If the declared return type of the interface
     * method is a primitive type, then the value returned by
     * this method must be an instance of the corresponding primitive
     * wrapper class; otherwise, it must be a type assignable to the
     * declared return type.  If the value returned by this method is
     * {@code null} and the interface method's return type is
     * primitive, then a {@code NullPointerException} will be
     * thrown by the method invocation on the proxy instance.  If the
     * value returned by this method is otherwise not compatible with
     * the interface method's declared return type as described above,
     * a {@code ClassCastException} will be thrown by the method
     * invocation on the proxy instance.
     * @throws Throwable the exception to throw from the method
     *                   invocation on the proxy instance.  The exception's type must be
     *                   assignable either to any of the exception types declared in the
     *                   {@code throws} clause of the interface method or to the
     *                   unchecked exception types {@code java.lang.RuntimeException}
     *                   or {@code java.lang.Error}.  If a checked exception is
     *                   thrown by this method that is not assignable to any of the
     *                   exception types declared in the {@code throws} clause of
     *                   the interface method, then an
     *                   {@link UndeclaredThrowableException} containing the
     *                   exception that was thrown by this method will be thrown by the
     *                   method invocation on the proxy instance.
     * @see UndeclaredThrowableException
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Property property = method.getAnnotation(Property.class);
        if(null != property) {
            if(method.getDeclaringClass().equals(IProxyInterface.class)) {
                return properties.get((String) (args[0]));
            }
            else {
                if("".equals(property.name())) {
                    return properties.get(method.getName());
                }
                else {
                    return properties.get(property.name());
                }
            }
        }
        com.dianping.midasx.world.annotation.Method methodAnnotation = method.getAnnotation(com.dianping.midasx.world.annotation.Method.class);
        if(null != methodAnnotation) {
            if(method.getDeclaringClass().equals(IProxyInterface.class)) {
                return cluster.invoke(new Identity<Object>(properties.get(cluster.idField)), (String) (args[0]), args);
            }
            else {
                return cluster.invoke(new Identity<Object>(properties.get(cluster.idField)), method.getName(), args);
            }
        }
        com.dianping.midasx.world.annotation.Relation relationdAnnotation = method.getAnnotation(com.dianping.midasx.world.annotation.Relation.class);
        if(null != relationdAnnotation) {
            Relation relation = null;
            if(method.getDeclaringClass().equals(IProxyInterface.class)) {
                relation = cluster.get((String) (args[0]));
                if(null == relation) {
                    return null;
                }
                Condition condition = relation.deduce(properties);
                if(relation.isSingle()) {
                    return cluster.find((Class<?>) args[1], condition);
                }
                return cluster.finds((Class<?>) args[1], condition);
            }
            else {
                relation = cluster.get(method.getName());
                if(null == relation) {
                    return null;
                }
                Condition condition = relation.deduce(properties);
                if(relation.isSingle()) {
                    return cluster.find((Class<?>) args[0], condition);
                }
                return cluster.finds((Class<?>) args[0], condition);
            }
        }
        return null;
    }
}
