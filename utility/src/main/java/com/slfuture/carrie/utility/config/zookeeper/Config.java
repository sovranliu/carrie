package com.slfuture.carrie.utility.config.zookeeper;

import com.slfuture.carrie.base.model.Path;
import com.slfuture.carrie.base.text.Text;
import com.slfuture.carrie.base.type.Map;
import com.slfuture.carrie.base.type.Set;
import com.slfuture.carrie.base.type.StringMixedMapping;
import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.base.type.core.IMap;
import com.slfuture.carrie.base.type.core.ISet;
import com.slfuture.carrie.base.type.core.ITable;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.utility.config.core.IConfigWatcher;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * 配置对象
 */
public class Config extends StringMixedMapping<String> implements IConfig {
    /**
     * 日志对象
     */
    protected static Logger logger = Logger.getLogger(Config.class);
    /**
     * Zookeeper节点
     */
    protected ZooKeeper node = null;
    /**
     * 路径
     */
    protected Path path = null;
    /**
     * 属性映射
     */
    protected IMap<String, String> properties = new Map<String, String>();
    /**
     * 监视者集合
     */
    protected ISet<IConfigWatcher> watchers = new Set<IConfigWatcher>();


    /**
     * 构造函数
     */
    public Config() { }

    /**
     * 获取指定键的值
     *
     * @param name 键名称
     * @return 值
     */
    @Override
    public String get(String name) {
        return properties.get(name);
    }

    /**
     * 遍历指定路径的节点
     *
     * @param path 路径
     * @return 指定路径下的节点，不存在返回null
     */
    @Override
    public IConfig visit(String path) {
        IConfig result = this;
        for(String step : path.split("/")) {
            if(Text.isBlank(step)) {
                continue;
            }
            if(Path.PATH_PARENT.equals(step)) {
                result = ((Config) result).parent;
            }
            else if(Path.PATH_CURRENT.equals(step)) {
                continue;
            }
            else {
                ISet<IConfig> set = ((Config) result).children.get(step);
                if(null == set || 0 == set.size()) {
                    return null;
                }
                result = set.offer();
            }
        }
        return result;
    }

    /**
     * 遍历指定路径的节点集
     *
     * @param path 路径
     * @return 指定路径下的节点集，不存在返回空集合
     */
    @Override
    public ISet<IConfig> visits(String path) {
        ISet<IConfig> result = new Set<IConfig>(this);
        for(String step : path.split("/")) {
            if(Text.isBlank(step)) {
                continue;
            }
            if(Path.PATH_PARENT.equals(step)) {
                result.clear();
                result.add(((Config) result.offer()).parent);
            }
            else if(Path.PATH_CURRENT.equals(step)) {
                continue;
            }
            else {
                ISet<IConfig> set = ((Config) result.offer()).children.get(step);
                if(null == set || 0 == set.size()) {
                    return new Set<IConfig>();
                }
                result = set;
            }
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
        if(0 == watchers.size()) {
            try {
                node.exists("/" + path.toString("/"), new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        if(Event.EventType.None != event.getType()) {
                            for(IConfigWatcher watcher : watchers) {
                                try {
                                    watcher.onChanged(Config.this);
                                }
                                catch(Exception e) {
                                    logger.error("config watch callback failed", e);
                                }
                            }
                            return;
                        }
                        try {
                            node.exists("/" + path.toString("/"), this);
                        }
                        catch (Exception e) {
                            logger.error("watch zookeeper failed", e);
                        }
                    }
                });
            }
            catch (Exception e) {
                logger.error("watch zookeeper failed", e);
            }
        }
        for(IConfigWatcher confWatch : watchers) {
            if(confWatch == watcher) {
                return;
            }
        }
        watchers.add(watcher);
    }
}
