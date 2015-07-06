package com.dianping.midasx.world.condition;

import com.dianping.midasx.world.IObject;
import com.dianping.midasx.world.facade.FacadeObject;

/**
 * 字段关系
 */
public class FieldRelation<T extends Comparable<T>> {
    /**
     * 本地字段
     */
    public String local;
    /**
     * 对方字段
     */
    public String remote;


    /**
     * 生成字段比较器
     *
     * @param o 本地对象
     * @return 字段比较器
     */
    public FieldComparator<T> deduce(IObject o) {
        FieldComparator<T> result = new FieldComparator<T>();
        result.value = FacadeObject.field(o, local);
        result.field = remote;
        return result;
    }

    /**
     * 转换为字符串
     *
     * @return 字符串
     */
    @Override
    public  String toString() {
        return local + " = " + remote;
    }
}
