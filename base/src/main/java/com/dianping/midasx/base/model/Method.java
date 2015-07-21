package com.dianping.midasx.base.model;

import com.dianping.midasx.base.text.Text;

import java.util.ArrayList;

/**
 * 调用方法
 */
public class Method {
    /**
     * 名称
     */
    public String name;
    /**
     * 参数类型列表
     */
    public Class<?>[] parameters;


    /**
     * 构造函数
     */
    public Method() { }

    /**
     * 构造函数
     *
     * @param name 方法名称
     * @param parameters 参数类型列表
     */
    public Method(String name, Class<?>[] parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    /**
     * 构建方法对象
     *
     * @param string 字符串
     * @return 方法对象
     */
    public static Method build(String string) {
        Method result = new Method();
        result.name = Text.substring(string, null, "(").trim();
        String parameterString = Text.substring(string, "(", ")");
        ArrayList<Class<?>> list = new ArrayList<Class<?>>();
        for(String parameter : parameterString.split(",")) {
            try {
                list.add(Class.forName(parameter.trim()));
            }
            catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        result.parameters = list.toArray(new Class<?>[0]);
        return result;
    }

    /**
     * 转为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        if(null == parameters) {
            builder.append("()");
            return builder.toString();
        }
        boolean sentry = false;
        builder.append("(");
        for(Class<?> parameter : parameters) {
            if(sentry) {
                builder.append(",");
            }
            else {
                sentry = true;
            }
            builder.append(parameter);
        }
        builder.append(")");
        return builder.toString();
    }
}
