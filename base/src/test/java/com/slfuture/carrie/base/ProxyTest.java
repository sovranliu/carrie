package com.slfuture.carrie.base;

import java.lang.reflect.Proxy;

/**
 *
 */
public class ProxyTest {
    /**
     *
     */
    public static void main(String argv[]) {
        TestInvocationHandler handler = new TestInvocationHandler();
        TestProxyClass proxy = (TestProxyClass) Proxy.newProxyInstance(
                TestProxyClass.class.getClassLoader(),
                new Class<?>[] {TestProxyClass.class},
                handler);
        proxy.call(1);
    }
}
