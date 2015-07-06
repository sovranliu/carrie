package com.dianping.midasx.base.model.core;

/**
 * 模块接口
 */
public interface IModule {
    /**
     * 初始化
     *
     * @return 是否初始化成功
     */
    public boolean initialize();

    /**
     * 终止
     */
    public void terminate();
}
