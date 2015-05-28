package com.slfuture.carrie.utility.config.xml;

import com.slfuture.carrie.base.type.Set;
import com.slfuture.carrie.base.type.StringMixedMapping;
import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.base.type.core.ISet;
import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.utility.config.core.IConfigWatcher;

/**
 * 配置对象
 */
public class Config extends StringMixedMapping<String> implements IConfig {
    /**
     * XML节点
     */
    protected IXMLNode node = null;


    /**
     * 构造函数
     */
    public Config() { }

    /**
     * 构造函数
     *
     * @param node XML配置节点
     */
    protected Config(IXMLNode node) {
        this.node = node;
    }

    /**
     * 获取指定键的值
     *
     * @param name 键名称
     * @return 值
     */
    @Override
    public String get(String name) {
        return node.get(name);
    }

    /**
     * 遍历指定路径的节点
     *
     * @param path 路径
     * @return 指定路径下的节点，不存在返回null
     */
    @Override
    public IConfig visit(String path) {
        IXMLNode result = node.visit(path);
        if(null == result) {
            return null;
        }
        return new Config(result);
    }

    /**
     * 遍历指定路径的节点集
     *
     * @param path 路径
     * @return 指定路径下的节点集，不存在返回空集合
     */
    @Override
    public ISet<IConfig> visits(String path) {
        ICollection<IXMLNode> nodes = node.visits(path);
        if(null == nodes) {
            return null;
        }
        ISet<IConfig> result = new Set<IConfig>();
        for(IXMLNode node : nodes) {
            result.add(new Config(node));
        }
        return result;
    }

    /**
     * 监听配置变化
     *
     * @param watcher 监视者
     * @throws UnsupportedOperationException 部分配置不支持该操作
     */
    @Override
    public void watch(IConfigWatcher watcher) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("com.slfuture.carrie.utility.config.xml.RootConfig not support watch operation.");
    }
}
