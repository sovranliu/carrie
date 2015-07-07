package com.dianping.midasx.utility;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 15-6-28.
 */
public class Test2 {
    public static class Handler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Annotation[] a = method.getAnnotations();
            Property aa = method.getAnnotation(Property.class);
            aa.name();
            return null;
        }
    }

    /**
     * 主函数
     */
    public static void main(String[] argv) throws Exception {
        Handler handler = new Handler();
        TInterface t = (TInterface) (Proxy.newProxyInstance(TInterface.class.getClassLoader(), new Class[]{TInterface.class}, handler));
        t.cancel();
    }
}
