package com.slfuture.carrie.utility;

import com.slfuture.carrie.utility.config.xml.RootConfig;
import com.slfuture.carrie.utility.db.DBConnectionPoolConfig;
import com.slfuture.carrie.utility.db.DBExecutor;
import com.slfuture.carrie.utility.db.SQLExecutor;
import com.slfuture.carrie.utility.template.Context;
import com.slfuture.carrie.utility.template.VelocityTemplate;

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

    public static class A {
        public String b() {
            return "11";
        }
    }

    /**
     * 主函数
     */
    public static void main(String[] argv) throws Exception {
        String sql = "UPDATE A_Bid SET Status = 6 WHERE Status IN(3, 4, 5) AND ID = 1";
        DBExecutor exe = new DBExecutor();
        DBConnectionPoolConfig conf = new DBConnectionPoolConfig();
        RootConfig root = new RootConfig();
        root.load("\\F:\\db.xml");
        conf.load(root);
        exe.initialize(conf);
        int i = exe.alter(sql);
        i = exe.alter("UPDATE A_Bid SET Status = 6 WHERE ID = 2");
        i = exe.alter("UPDATE A_Bid SET Status = 6 WHERE ID = 2 AND  Status IN(3, 4, 5)");

        VelocityTemplate velocityTemplate = new VelocityTemplate();
        velocityTemplate.setContent("$a.b()");
        Context context = new Context();
        context.put("a", new A());
        System.out.println(velocityTemplate.render(context));

        Handler handler = new Handler();
        TInterface t = (TInterface) (Proxy.newProxyInstance(TInterface.class.getClassLoader(), new Class[]{TInterface.class}, handler));
        t.cancel();
    }
}
