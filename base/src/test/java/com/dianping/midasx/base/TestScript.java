package com.dianping.midasx.base;

import javax.script.*;

/**
 * 脚本测试
 */
public class TestScript {
    public static void does() {
        System.out.println("TestScript.does()");
    }

    /**
     * 启动函数
     */
    public static void main(String[] argv) throws Exception {
        int $ = 1;
        System.out.println($);

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");

        // TestProxyClass proxy = (TestProxyClass) Proxy.newProxyInstance(TestProxyClass.class.getClassLoader(), new Class[] {TestProxyClass.class}, new TestInvocationHandler());
        // proxy.call("");

        // engine.put("v", proxy);
        SimpleScriptContext context = new SimpleScriptContext();
        engine.setContext(context);
        // engine.put("TestScript", TestScript.class);
        engine.eval("com.slfuture.carrie.base.TestScript.does();");
        // engine.eval("function f1(a) { return a + 1;}");
        ScriptContext sc = engine.getContext();
        CompiledScript c1 = ((Compilable) engine).compile("var x = 9; function f2(a){return a - 5;} f1(5) + 2;");
        Object result = c1.eval();
        engine.setContext(new SimpleScriptContext());
        CompiledScript c2 = ((Compilable) engine).compile("f2(5) + x;");
        result = c2.eval();
        // Object result = engine.eval("f(5);");
        System.out.println(result);
    }
}
