package com.dianping.midasx.base.interaction.core;

/**
 * 监听接口
 */
public interface IListener<T, D> {
    /**
     * 监听回调
     *
     * @param target 目标
     * @param data 内容
     */
    public void onRead(T target, D data);
}
