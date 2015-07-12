package com.dianping.midasx.world;

import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.safe.Table;
import com.dianping.midasx.utility.config.Configuration;
import com.dianping.midasx.utility.config.core.IConfig;
import com.dianping.midasx.world.relation.Condition;
import org.apache.log4j.Logger;

import java.text.ParseException;

/**
 * 世界类
 */
public class World {
    /**
     * 日志对象
     */
    public static Logger logger = Logger.getLogger(World.class);
    /**
     * 簇表
     */
    private static Table<String, Cluster> clusters = new Table<String, Cluster>();


    /**
     * 隐藏构造函数
     */
    private World() { }

    /**
     * 获取指定名称的簇
     *
     * @param name 簇名
     * @return 簇
     */
    public static Cluster getCluster(String name) {
        Cluster result = clusters.get(name);
        if(null != result) {
            return result;
        }
        IConfig conf = Configuration.root().visit("/world/clusters/" + name);
        if(null == conf) {
            return null;
        }
        try {
            result = Cluster.build(conf);
            clusters.put(name, result);
        }
        catch (ParseException e) {
            logger.error("call Cluster.build(" + conf + ") failed", e);
        }
        return result;
    }

    /**
     * 加载指定条件的对象
     *
     * @param clazz 对象类型
     * @param clusterName 簇名称
     * @param condition 查找条件
     * @return 被查找的指定对象
     */
    public static <T> T find(Class<T> clazz, String clusterName, Condition condition) {
        Cluster cluster = getCluster(clusterName);
        if(null == cluster) {
            return null;
        }
        return cluster.find(clazz, condition);
    }

    /**
     * 加载指定条件的对象集
     *
     * @param clazz 对象类型
     * @param clusterName 簇名称
     * @param condition 查找条件
     * @return 被查找的指定对象集
     */
    public static <T> ICollection<T> finds(Class<T> clazz, String clusterName, Condition condition) {
        Cluster cluster = getCluster(clusterName);
        if(null == cluster) {
            return null;
        }
        return cluster.finds(clazz, condition);
    }
}
