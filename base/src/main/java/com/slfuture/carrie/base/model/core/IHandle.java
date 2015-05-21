package com.slfuture.carrie.base.model.core;

/**
 * 句柄接口
 */
public interface IHandle {
    /**
     * 打开句柄
     *
     * @return 是否打开成功
     */
    public boolean open();

    /**
     * 关闭句柄
     */
    public void close();
}
