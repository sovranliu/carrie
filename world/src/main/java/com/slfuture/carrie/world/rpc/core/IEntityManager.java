package com.slfuture.carrie.world.rpc.core;

import com.slfuture.carrie.base.model.GlobeIdentity;

/**
 * 对象持有接口
 */
public interface IEntityManager {
    /**
     * 加载符合条件的对象
     *
     * @param id 标志符
     * @return 符合条件的对象
     */
    public Object load(GlobeIdentity<Object> id);
}
