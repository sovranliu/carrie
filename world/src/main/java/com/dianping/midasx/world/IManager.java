package com.dianping.midasx.world;

import com.dianping.midasx.base.logic.core.IHint;
import com.dianping.midasx.base.type.core.ICollection;

/**
 * 管理器接口
 */
public interface IManager<T extends IObject> {
    /**
     * 按条件查找单个对象
     *
     * @param hint 线索
     * @return 查找结果，搜索不到返回null
     */
    public T find(IHint hint);

    /**
     * 条件查找多个对象
     *
     * @param hint 线索
     * @return 查找到的对象集
     */
    public ICollection<T> finds(IHint hint);
}
