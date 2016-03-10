package com.slfuture.carrie.base.model.core;

/**
 * 可触发接口
 */
public interface IEventable<E> {
    /**
     * 事件回调
     *
     * @param event 事件
     */
    public void on(E event);
}
