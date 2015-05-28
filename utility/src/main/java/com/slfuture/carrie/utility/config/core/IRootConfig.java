package com.slfuture.carrie.utility.config.core;

/**
 * 根配置对象接口
 */
public interface IRootConfig extends IConfig {
    /**
     * 加载根配置
     *
     * @param uri 路径
     * @return 是否执行成功
     */
    public boolean load(String uri);
}
