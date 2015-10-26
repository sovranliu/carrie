package com.slfuture.carrie.lightning.proxy;

import com.slfuture.carrie.world.IObject;

/**
 * 对象代理类
 */
public class ObjectProxy {
    /**
     * 代理对象
     */
    private IObject object = null;


    /**
     * 构造函数
     *
     * @param object 对象
     */
    public ObjectProxy(IObject object) {
        this.object = object;
    }

    /**
     * 获取指定属性的值
     *
     * @param property 外联字符串
     * @return 值对象，可以是值类型，也可以是代理对象
     */
    public Object p(String property) {
        return object.property(property);
    }

    /**
     * 按照指定条件搜索对象
     *
     * @param name 关系名称
     * @return 结果对象或结果对象集合
     */
    public Object r(String name) {
        Object relative = object.relative(name);
        if(null == relative) {
            return null;
        }
        if(relative.getClass().isArray()) {
            Object[] array = (Object[]) relative;
            ObjectProxy[] results = new ObjectProxy[array.length];
            int i = 0;
            for(Object item : array) {
                results[i++] = new ObjectProxy((IObject) item);
            }
            return results;
        }
        return new ObjectProxy((IObject) relative);
    }

    /**
     * 调用指定方法
     *
     * @param method 方法名称
     * @param parameters 参数列表
     * @return 返回值
     */
    public Object c(String method, Object...parameters) {
        Class<?>[] parameterTypes = new Class<?>[parameters.length];
        int i = 0;
        for(Object parameter : parameters) {
            if(null == parameter) {
                parameterTypes[i++] = Object.class;
            }
            else if(Double.class.equals(parameter.getClass())) {
                double doubleValue = (Double) parameter;
                if(doubleValue % 1 > 0) {
                    parameterTypes[i++] = parameter.getClass();
                }
                else {
                    parameters[i] = (int) doubleValue;
                    parameterTypes[i++] = int.class;
                }
            }
            else {
                parameterTypes[i++] = parameter.getClass();
            }
        }
        com.slfuture.carrie.base.model.Method modelMethod = new com.slfuture.carrie.base.model.Method(method, parameterTypes);
        return object.invoke(modelMethod, parameters);
    }
}
