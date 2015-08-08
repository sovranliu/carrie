package com.slfuture.carrie.world.handler;

import com.slfuture.carrie.base.type.List;
import com.slfuture.carrie.base.type.Set;
import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.world.IObject;
import com.slfuture.carrie.world.World;
import com.slfuture.carrie.world.annotation.Property;
import com.slfuture.carrie.world.annotation.Relative;
import com.slfuture.carrie.world.logic.Agent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 对象句柄
 */
public class ObjectHandler implements InvocationHandler {
    /**
     * 调用类型
     */
    public final static int INVOKE_TYPE_UNKNOWEN = 0;
    public final static int INVOKE_TYPE_PROPERTY = 1;
    public final static int INVOKE_TYPE_RELATIVE= 2;
    public final static int INVOKE_TYPE_METHOD = 3;


    /**
     * 适配对象
     */
    public Agent agent = null;


    /**
     * 构造函数
     */
    public ObjectHandler() { }
    public ObjectHandler(Agent agent) {
        this.agent = agent;
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
            String propertyName = null;
            if("".equals(property.name())) {
                propertyName = method.getName();
            }
            else {
                propertyName = property.name();
            }
            return invoke(null, ObjectHandler.INVOKE_TYPE_PROPERTY, new com.slfuture.carrie.base.model.Method(propertyName, null), null);
        }
        com.slfuture.carrie.world.annotation.Method methodAnnotation = method.getAnnotation(com.slfuture.carrie.world.annotation.Method.class);
        if(null != methodAnnotation) {
            com.slfuture.carrie.base.model.Method methodModel = new com.slfuture.carrie.base.model.Method();
            methodModel.name = method.getName();
            methodModel.parameters = method.getParameterTypes();
            return invoke(null, ObjectHandler.INVOKE_TYPE_METHOD, methodModel, args);
        }
        Relative relationdAnnotation = method.getAnnotation(Relative.class);
        if(null != relationdAnnotation) {
            return invoke((Class<?>)args[0], ObjectHandler.INVOKE_TYPE_RELATIVE, new com.slfuture.carrie.base.model.Method(method.getName(), null), null);
        }
        return null;
    }

    /**
     * 调用
     *
     * @param clazz 代理类
     * @param invokeType 调用类型
     * @param method 方法
     * @param args 参数列表
     * @return 调用结果
     */
    protected Object invoke(Class<?> clazz, int invokeType, com.slfuture.carrie.base.model.Method method, Object[] args) throws Throwable {
        if(ObjectHandler.INVOKE_TYPE_PROPERTY == invokeType) {
            return agent.property(method.name);
        }
        else if(ObjectHandler.INVOKE_TYPE_RELATIVE == invokeType) {
            if(World.getCluster(agent.clusterName).get(method.name).isSingle()) {
                return convert(agent.relative(method.name), clazz);
            }
            else {
                return converts(agent.relatives(method.name), clazz);
            }
        }
        else if(ObjectHandler.INVOKE_TYPE_METHOD == invokeType) {
            return agent.invoke(method, args);
        }
        return null;
    }

    /**
     * 适配对象转用户对象
     *
     * @param agent 适配对象
     * @param clazz 用户类
     * @return 用户对象
     */
    public <T> T convert(Agent agent, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new ObjectHandler(agent));
    }

    /**
     * 适配对象集合转用户对象集合
     *
     * @param adapters 适配对象集合
     * @param clazz 用户类
     * @return 用户对象集合
     */
    public <T> T[] converts(ICollection<Agent> adapters, Class<T> clazz) {
        Object[] result = new Object[adapters.size()];
        int i = 0;
        for(Agent agent : adapters) {
            result[i++] = convert(agent, clazz);
        }
        return (T[]) result;
    }
}
