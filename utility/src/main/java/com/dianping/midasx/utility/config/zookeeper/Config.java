package com.dianping.midasx.utility.config.zookeeper;

import com.dianping.midasx.base.type.Set;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.ISet;
import com.dianping.midasx.utility.config.core.IConfig;
import org.apache.log4j.Logger;

/**
 * 配置对象
 */
public class Config extends com.dianping.midasx.utility.config.Config implements IConfig {
    /**
     * 日志对象
     */
    protected static Logger logger = Logger.getLogger(Config.class);
    /**
     * 节点名称
     */
    protected String name = null;


    /**
     * 遍历指定路径的节点
     *
     * @param path 路径
     * @return 指定路径下的节点，不存在返回null
     */
    @Override
    public IConfig visit(String path) {
        if(null == path || path.equals("")) {
            return this;
        }
        String head = null;
        String tail = null;
        // 取出本次分析的段
        int i = path.indexOf(CONFIG_PATH_SEPARATOR);
        if(-1 == i) {
            head = path;
        }
        else {
            head = path.substring(0, i) ;
            tail = path.substring(i + 1);
        }
        ISet<IConfig> set = children.get(head);
        if(null != set) {
            return set.offer();
        }
        Config conf = getRoot().zget(getRelativePath() + "/" + head);
        if(null == conf) {
            return null;
        }
        conf.parent = this;
        return conf;
    }

    /**
     * 遍历指定路径的节点集
     *
     * @param path 路径
     * @return 指定路径下的节点集，不存在返回空集合
     */
    @Override
    public ISet<IConfig> visits(String path) {
        if(null == path || path.equals("")) {
            return new Set<IConfig>();
        }
        String head = null;
        String tail = null;
        // 取出本次分析的段
        int i = path.indexOf(CONFIG_PATH_SEPARATOR);
        if(-1 == i) {
            head = path;
        }
        else {
            head = path.substring(0, i) ;
            tail = path.substring(i + 1);
        }
        ISet<IConfig> set = children.get(head);
        if(null != set) {
            return set;
        }
        ICollection<IConfig> confSet = getRoot().zgets(getRelativePath() + "/" + head);
        if(null == confSet) {
            return null;
        }
        // HashCode变化导致双重拷贝
        Set<IConfig> result = new Set<IConfig>();
        for(IConfig conf : confSet) {
            ((Config) conf).parent = this;
            result.add(conf);
        }
        return result;
    }

    /**
     * 获取根节点
     *
     * @return 根节点
     */
    public RootConfig getRoot() {
        Config conf = this;
        while(!(conf instanceof RootConfig)) {
            conf = (Config) conf.parent;
        }
        return (RootConfig) conf;
    }

    /**
     * 获取相对路径
     *
     * @return 相对路径
     */
    public String getRelativePath() {
        String result = null;
        Config conf = this;
        while(!(conf instanceof RootConfig)) {
            if(null == result) {
                result = conf.name;
            }
            else {
                result = conf.name + "/" + result;
            }
            conf = (Config) conf.parent;
        }
        return result;
    }
}
