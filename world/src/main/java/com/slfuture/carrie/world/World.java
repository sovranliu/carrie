package com.slfuture.carrie.world;

import com.slfuture.carrie.base.type.Set;
import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.base.type.safe.Table;
import com.slfuture.carrie.utility.config.Configuration;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.world.cluster.Cluster;
import com.slfuture.carrie.world.cluster.DBCluster;
import com.slfuture.carrie.world.cluster.LocalCluster;
import com.slfuture.carrie.world.cluster.RemoteCluster;
import com.slfuture.carrie.world.cluster.core.ICluster;
import com.slfuture.carrie.world.event.EventCenter;
import com.slfuture.carrie.world.handler.ObjectHandler;
import com.slfuture.carrie.world.handler.ProxyHandler;
import com.slfuture.carrie.world.logic.Agent;
import com.slfuture.carrie.world.relation.Condition;
import com.slfuture.carrie.world.relation.Relation;
import com.slfuture.carrie.world.relation.prepare.PropertyPrepare;
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
     * 类名和簇映射
     */
    private static Table<String, String> clazzMap = new Table<String, String>();


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
        conditon.prepareSelf = new PropertyPrepare(getCluster(clusterName).primaryKey());
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
        if(null == handler.agent) {
            return null;
        }
        return (T) (Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, handler));
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
            result.add((T) (Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, handler)));
        }
        return result;
    }

    /**
     * 获取指定名称的关系对象
     *
     * @param self 自己的节点
     * @param name 关系名称
     * @param clazz 关系节点的类型
     * @return 关系对象
     */
    public static <T> T relative(Object self, String name, Class<T> clazz) {
        String clusterName = clazzMap.get(self.getClass().getName());
        if(null == clusterName) {
            return null;
        }
        ICluster<?> cluster = clusters.get(clusterName);
        if(null == cluster) {
            return null;
        }
        Relation relation = cluster.get(name);
        if(null == relation) {
            return null;
        }
        Condition condition = relation.deduce(self);
        return get(relation.cluster, condition, clazz);
    }

    /**
     * 获取指定名称的关系对象
     *
     * @param self 自己的节点
     * @param name 关系名称
     * @param clazz 关系节点的类型
     * @return 关系对象
     */
    public static <T> ICollection<T> relatives(Object self, String name, Class<T> clazz) {
        String clusterName = getClusterName(self);
        if(null == clusterName) {
            return null;
        }
        ICluster<?> cluster = clusters.get(clusterName);
        if(null == cluster) {
            return null;
        }
        Relation relation = cluster.get(name);
        if(null == relation) {
            return null;
        }
        Condition condition = relation.deduce(self);
        return gets(relation.cluster, condition, clazz);
    }

    /**
     * 抛出事件
     *
     * @param self 自身对象
     * @param event 事件对象
     */
    public static void throwEvent(Object self, IEvent event) {
        EventCenter.throwEvent(self, event);
    }

    /**
     * 获取实例对象的簇名
     *
     * @param self 对象
     * @return 簇名
     */
    public static String getClusterName(Object self) {
        return clazzMap.get(self.getClass().getName());
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
                clazzMap.put(conf.get("class"), name);
            }
            else {
                result = new RemoteCluster();
            }
        }
        else if(null != conf.get("table")) {
            result = new DBCluster();
            String table = conf.get("table");
            ((DBCluster) result).dbName = table.substring(0, table.indexOf("."));
            ((DBCluster) result).tableName = table.substring(table.indexOf(".") + 1);
            ((DBCluster) result).parse(conf);
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
        for(IConfig confSon : conf.visits("catcher/catch")) {
            try {
                EventCenter.createPipe(name, confSon);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return result;
    }
}
