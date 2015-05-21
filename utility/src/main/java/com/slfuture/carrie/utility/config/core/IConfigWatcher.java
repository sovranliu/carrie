package com.slfuture.carrie.utility.config.core;

/**
 * 配置监听者接口
 */
public interface IConfigWatcher {
    /**
     * 配置变动回调
     */
    public void onChanged();
}
