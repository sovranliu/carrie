package com.dianping.midasx.world.cluster;

import com.dianping.midasx.base.logic.ComparisonTool;
import com.dianping.midasx.base.model.Method;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.world.relation.Condition;
import com.dianping.midasx.world.relation.prepare.PropertyPrepare;

import java.util.NoSuchElementException;

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
     * @param id     被调用对象标志符
     * @param method 方法
     * @param args   参数列表
     * @return 调用结果
     */
    public Object invoke(Object id, Method method, Object... args) {
        Condition condition = new Condition();
        condition.compareType = ComparisonTool.COMPARETYPE_EQUAL;
        condition.prepareSelf = new PropertyPrepare(primaryKey);
        Object result = find(condition);
        if(null == result) {
            throw new NoSuchElementException("cluster " + name + " find " + condition.toString() +  " failed");
        }
        try {
            return result.getClass().getDeclaredMethod(method.name, method.parameters).invoke(result, args);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
