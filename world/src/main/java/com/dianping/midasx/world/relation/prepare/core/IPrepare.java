package com.dianping.midasx.world.relation.prepare.core;

import com.dianping.midasx.base.model.core.IFilter;

/**
 * 比较准备接口
 */
public interface IPrepare extends IFilter<Object, Object>, Cloneable {
    /**
     * 克隆
     *
     * @return 克隆对象
     */
    public Object clone() throws CloneNotSupportedException;
}
