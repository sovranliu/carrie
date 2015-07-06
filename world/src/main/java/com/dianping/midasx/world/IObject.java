package com.dianping.midasx.world;

import com.dianping.midasx.base.model.GlobeIdentity;

/**
 * 世界对象接口
 *
 * 具备属性，关系，方法，事件特性的对象
 */
public interface IObject {
    /**
     * 获取标志
     *
     * @return 获取标志
     */
    public GlobeIdentity<Object> identity();
}
