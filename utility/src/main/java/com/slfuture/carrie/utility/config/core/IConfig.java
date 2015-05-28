package com.slfuture.carrie.utility.config.core;

import com.slfuture.carrie.base.type.core.IMixedMapping;
import com.slfuture.carrie.base.type.core.ISet;

/**
 * 配置对象接口
 *
 * 基于有向图结构的配置体系
 */
public interface IConfig extends IMixedMapping<String, String> {
    /**
     * 遍历指定路径的节点
     *
     * @param path 路径
     * @return 指定路径下的节点，不存在返回null
     */
    public IConfig visit(String path);

    /**
     * 遍历指定路径的节点集
     *
     * @param path 路径
     * @return 指定路径下的节点集，不存在返回空集合
     */
    public ISet<IConfig> visits(String path);

    /**
     * 监听配置变化
     *
     * @param watcher 监视者
     * @throws UnsupportedOperationException 部分配置不支持该操作
     */
    public void watch(IConfigWatcher watcher) throws UnsupportedOperationException;
}
