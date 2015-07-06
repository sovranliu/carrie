package com.dianping.midasx.base.model.core;

/**
 * 有状态的接口
 */
public interface IStatus<T> {
    /**
     * 当前状态
     *
     * @return 状态
     */
    public T status();
}
