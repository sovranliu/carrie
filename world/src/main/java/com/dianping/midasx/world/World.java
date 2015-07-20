package com.dianping.midasx.world;

import com.dianping.midasx.base.type.Set;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.safe.Table;
import com.dianping.midasx.utility.config.Configuration;
import com.dianping.midasx.utility.config.core.IConfig;
import com.dianping.midasx.world.cluster.Cluster;
import com.dianping.midasx.world.cluster.DBCluster;
import com.dianping.midasx.world.cluster.LocalCluster;
import com.dianping.midasx.world.cluster.RemoteCluster;
import com.dianping.midasx.world.cluster.core.ICluster;
import com.dianping.midasx.world.handler.ObjectHandler;
import com.dianping.midasx.world.handler.ProxyHandler;
import com.dianping.midasx.world.logic.Agent;
import com.dianping.midasx.world.relation.Condition;
import com.dianping.midasx.world.relation.Relation;
import com.dianping.midasx.world.relation.prepare.AgentPrepare;
import org.apache.log4j.Logger;

import java.lang.reflect.Proxy;
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
    private static Table<String, ICluster<?>> clusters = new Table<String, ICluster<?>>();


    /**
     * 隐藏构造函数
     */
    private World() { }


    /**
     * 获取指定条件的单个对象
     *
     * @param clusterName 簇名称
     * @param id 标志符
     * @param clazz 目标类
     * @return 查找到的目标对象
     */
    public static <T> T get(String clusterName, Object id, Class<T> clazz) {
        Condition conditon = new Condition();
        conditon.prepareSelf = new AgentPrepare(getCluster(clusterName).primaryKey());
        conditon.target = id;
        return get(clusterName, conditon, clazz);
    }

    /**
     * 获取指定条件的单个对象
     *
     * @param clusterName 簇名称
     * @param condition 查找条件
     * @param clazz 目标类
     * @return 查找到的目标对象
     */
    public static <T> T get(String clusterName, Condition condition, Class<T> clazz) {
        ObjectHandler handler = null;
        if(clazz.equals(IObject.class)) {
            handler = new ProxyHandler();
        }
        else {
            handler = new ObjectHandler();
        }
        handler.agent = getCluster(clusterName).findAgent(condition);
        return (T) (Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), handler));
    }

    /**
     * 获取指定条件的对象集
     *
     * @param clusterName 簇名称
     * @param condition 查找条件
     * @param clazz 目标类
     * @return 查找到的目标对象集
     */
    public static <T> ICollection<T> gets(String clusterName, Condition condition, Class<T> clazz) {
        Set<T> result = new Set<T>();
        for(Agent agent : getCluster(clusterName).findAgents(condition)) {
            ObjectHandler handler = null;
            if(clazz.equals(IObject.class)) {
                handler = new ProxyHandler();
            }
            else {
                handler = new ObjectHandler();
            }
            handler.agent = agent;
            result.add((T) (Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), handler)));
        }
        return result;
    }

    /**
     * 获取指定名称的簇
     *
     * @param name 簇名
     * @return 簇
     */
    public static ICluster<?> getCluster(String name) {
        ICluster<?> result = clusters.get(name);
        if(null != result) {
            return result;
        }
        result = buildCluster(name);
        if(null == result) {
            return null;
        }
        clusters.put(name, result);
        return result;
    }

    /**
     * 构建簇对象
     *
     * @param name 簇名称
     * @return 簇对象
     */
    private static ICluster<?> buildCluster(String name) {
        IConfig conf = Configuration.root().visit("/world/clusters/" + name);
        if(null == conf) {
            return null;
        }
        Cluster<?> result = null;
        if(null != conf.get("class")) {
            boolean isLocal = true;
            try {
                Class.forName(conf.get("class"));
            }
            catch (ClassNotFoundException e) {
                isLocal = false;
            }
            if(isLocal) {
                result = new LocalCluster();
                ((LocalCluster) result).className = conf.get("class");
            }
            else {
                result = new RemoteCluster();
            }
        }
        else if(null != conf.get("table")) {
            result = new DBCluster();
            String table = conf.get("table");
            if (null != table) {
                ((DBCluster) result).dbName = table.substring(0, table.indexOf("."));
                ((DBCluster) result).tableName = table.substring(table.indexOf(".") + 1);
            }
        }
        result.name = name;
        result.primaryKey = conf.get("primarykey");
        for(IConfig confSon : conf.visits("relations/relation")) {
            try {
                result.put(confSon.get("name"), Relation.build(confSon));
            }
            catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
