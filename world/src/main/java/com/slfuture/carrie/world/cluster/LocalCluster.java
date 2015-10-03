package com.slfuture.carrie.world.cluster;

import com.slfuture.carrie.base.model.Method;
import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.world.relation.Condition;

/**
 * 本地簇类
 */
public class LocalCluster extends Cluster<Object> {
    /**
     * 类名称
     */
    public String className;


    /**
     * 查找符合条件的对象
     *
     * @param condition 查找条件
     * @return 查找到的指定对象
     */
    public Object find(Condition condition) {
        try {
            Class<?> clazz = Class.forName(className);
            java.lang.reflect.Method method = clazz.getMethod("find", Condition.class);
            return method.invoke(null, condition);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 查找符合条件的对象集
     *
     * @param condition 查找条件
     * @return 查找到的对象集
     */
    public ICollection<Object> finds(Condition condition) {
        try {
            Class<?> clazz = Class.forName(className);
            java.lang.reflect.Method method = clazz.getMethod("finds", Condition.class);
            return (ICollection<Object>) (method.invoke(null, condition));
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 调用方法
     *
     * @param target 被调用对象
     * @param method 方法
     * @param args 参数列表
     * @return 调用结果
     */
    @Override
    public Object invoke(Object target, Method method, Object...args) {
        try {
            return target.getClass().getMethod(method.name, method.parameters).invoke(target, args);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
