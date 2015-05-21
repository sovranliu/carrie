package com.slfuture.carrie.utility.config.zookeeper;

import com.slfuture.carrie.base.model.Path;
import com.slfuture.carrie.base.text.Text;
import com.slfuture.carrie.base.type.Map;
import com.slfuture.carrie.base.type.Set;
import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.base.type.core.IMap;
import com.slfuture.carrie.base.type.core.ISet;
import com.slfuture.carrie.base.type.core.ITable;
import com.slfuture.carrie.utility.config.core.IConfig;
import org.apache.log4j.Logger;

/**
 * 配置对象
 */
public class Config implements IConfig {
    /**
     * 日志对象
     */
    protected static Logger logger = Logger.getLogger(Config.class);
    /**
     * 父节点
     */
    protected Config parent = null;
    /**
     * 属性映射
     */
    protected IMap<String, String> properties = new Map<String, String>();
    /**
     * 子节点映射
     */
    protected ITable<String, ISet<IConfig>> children = new Table<String, ISet<IConfig>>();


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
}
