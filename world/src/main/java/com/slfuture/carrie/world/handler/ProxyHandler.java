package com.slfuture.carrie.world.handler;

import com.slfuture.carrie.world.IObject;
import com.slfuture.carrie.world.annotation.Property;
import com.slfuture.carrie.world.annotation.Relative;
import com.slfuture.carrie.world.logic.Agent;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * 代理句柄类
 */
public class ProxyHandler extends ObjectHandler {
    /**
     * 构造函数
     */
    public ProxyHandler() { }
    public ProxyHandler(Agent agent) {
        this.agent = agent;
    }


    /**
     * 适配对象转用户对象
     *
     * @param agent 适配对象
     * @param clazz 用户类
     * @return 用户对象
     */
    @Override
    public <T> T convert(Agent agent, Class<T> clazz) {
        if(IObject.class.equals(clazz)) {
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ProxyHandler(agent));
        }
        return super.convert(agent, clazz);
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
            return invoke(null, ObjectHandler.INVOKE_TYPE_PROPERTY, new com.slfuture.carrie.base.model.Method((String)(args[0]), null), null);
        }
        com.slfuture.carrie.world.annotation.Method methodAnnotation = method.getAnnotation(com.slfuture.carrie.world.annotation.Method.class);
        if(null != methodAnnotation) {
            com.slfuture.carrie.base.model.Method methodModel = (com.slfuture.carrie.base.model.Method) (args[0]);
            //
            ArrayList<Object> arglist = new ArrayList<Object>();
            boolean sentry = false;
            for(Object object : args) {
                if(sentry) {
                    arglist.add(object);
                }
                else {
                    sentry = true;
                }
            }
            return invoke(null, ObjectHandler.INVOKE_TYPE_METHOD, methodModel, arglist.toArray(new Object[0]));
        }
        Relative relationdAnnotation = method.getAnnotation(Relative.class);
        if(null != relationdAnnotation) {
            Class<?> resultClass = IObject.class;
            if(args.length > 1) {
                resultClass = (Class<?>)args[1];
            }
            return invoke(resultClass, ObjectHandler.INVOKE_TYPE_RELATIVE, new com.slfuture.carrie.base.model.Method((String)(args[0]), null), null);
        }
        return null;
    }
}
