package com.slfuture.carrie.base.model;

import com.slfuture.carrie.base.text.Text;

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
     * 比较
     *
     * @return 是否等价
     */
    public boolean equals(java.lang.reflect.Method method) {
        if(!method.getName().equals(name)) {
            return false;
        }
        if(parameters.length != method.getParameterTypes().length) {
            return false;
        }
        for(int i = 0; i < parameters.length; i++) {
            if(!classEquals(parameters[i], method.getParameterTypes()[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 类对比
     *
     * @param self 自身类
     * @param other 被比较类
     * @return 是否相等
     */
    public static boolean classEquals(Class<?> self, Class<?> other) {
        if(byte.class.equals(self) || Byte.class.equals(self)) {
            if(byte.class.equals(other) || Byte.class.equals(other)) {
                return true;
            }
            return false;
        }
        else if(short.class.equals(self) || Short.class.equals(self)) {
            if(short.class.equals(other) || Short.class.equals(other)) {
                return true;
            }
            return false;
        }
        else if(int.class.equals(self) || Integer.class.equals(self)) {
            if(int.class.equals(other) || Integer.class.equals(other)) {
                return true;
            }
            return false;
        }
        else if(long.class.equals(self) || Long.class.equals(self)) {
            if(long.class.equals(other) || Long.class.equals(other)) {
                return true;
            }
            return false;
        }
        else if(float.class.equals(self) || Float.class.equals(self)) {
            if(float.class.equals(other) || Float.class.equals(other)) {
                return true;
            }
            return false;
        }
        else if(double.class.equals(self) || Double.class.equals(self)) {
            if(double.class.equals(other) || Double.class.equals(other)) {
                return true;
            }
            return false;
        }
        else if(char.class.equals(self) || Character.class.equals(self)) {
            if(char.class.equals(other) || Character.class.equals(other)) {
                return true;
            }
            return false;
        }
        else if(boolean.class.equals(self) || Boolean.class.equals(self)) {
            if(boolean.class.equals(other) || Boolean.class.equals(other)) {
                return true;
            }
            return false;
        }
        else {
            return self.equals(other);
        }
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
