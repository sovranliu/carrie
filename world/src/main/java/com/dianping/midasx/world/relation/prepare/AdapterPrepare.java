package com.dianping.midasx.world.relation.prepare;

import com.dianping.midasx.world.logic.Adapter;
import com.dianping.midasx.world.relation.prepare.core.IPrepare;

/**
 * 对象适配器准备类
 */
public class AdapterPrepare implements IPrepare {
    /**
     * 属性名
     */
    public String property = null;


    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    @Override
    public Object filter(Object origin) {
        if(null == origin) {
            return null;
        }
        Adapter adapter = (Adapter) origin;
        return adapter.property(property);
    }

    /**
     * 克隆
     *
     * @return 克隆对象
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
