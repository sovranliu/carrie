package com.slfuture.carrie.base.model.core;

/**
 * 可触发接口
 */
public interface IEventable<T> {
    /**
     * 回调事件
     *
     * @param data 回调数据
     */
    public void on(T data);
}
