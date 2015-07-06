package com.dianping.midasx.base.interaction.core;

/**
 * 可监听接口
 */
public interface IListenable<D> {
    /**
     * 监听回调
     *
     * @param data 内容
     */
    public void onRead(D data);
}
