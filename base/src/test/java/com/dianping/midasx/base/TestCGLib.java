package com.dianping.midasx.base;

import net.sf.cglib.proxy.Enhancer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by Administrator on 15-3-9.
 */
public class TestCGLib {
    /**
     * 启动函数
     */
    public static void main(String[] argv) throws Exception {
        TestMethodInterceptor interceptor = new TestMethodInterceptor();
        Enhancer en = new Enhancer();
        //进行代理
        en.setSuperclass(Object.class);
        en.setCallback(interceptor);
        //生成代理实例
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        //
        // TestProxyClass proxy = (TestProxyClass) Proxy.newProxyInstance(TestProxyClass.class.getClassLoader(), new Class[]{}, new TestInvocationHandler());
        // proxy.call();
        //
        engine.put("v", en.create());
        Object result = engine.eval("v.does();");
        System.out.println(result);
    }
}
