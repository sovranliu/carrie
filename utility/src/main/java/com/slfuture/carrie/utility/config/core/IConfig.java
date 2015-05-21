package com.slfuture.carrie.utility.config.core;

import com.slfuture.carrie.base.type.core.ISet;

/**
 * 配置对象接口
 *
 * 基于有向图结构的配置体系
 */
public interface IConfig {
    /**
     * 获取指定键的值
     *
     * @param name 键名称
     * @return 值
     */
    public String get(String name);

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
}
