package com.dianping.midasx.base.model.core;

/**
 * 指针接口
 */
public interface IPointer<N> {
    /**
     * 本地
     *
     * @return 本地
     */
    public N local();

    /**
     * 对方
     *
     * @return 对方
     */
    public N remote();
}
