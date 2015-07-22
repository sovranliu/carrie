package com.slfuture.carrie.base.model.core;

/**
 * 过程接口
 */
public interface IProcedure {
    /**
     * 打开
     *
     * @return 是否打开成功
     */
    public boolean start();

    /**
     * 暂停
     */
    public void pause();

    /**
     * 继续
     */
    public void continues();

    /**
     * 停止
     */
    public void stop();
}
