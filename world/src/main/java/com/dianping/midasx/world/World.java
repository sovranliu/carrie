package com.dianping.midasx.world;

import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.safe.Table;
import com.dianping.midasx.utility.config.Configuration;
import com.dianping.midasx.utility.config.core.IConfig;
import com.dianping.midasx.world.cluster.Cluster;
import com.dianping.midasx.world.cluster.DBCluster;
import com.dianping.midasx.world.cluster.LocalCluster;
import com.dianping.midasx.world.cluster.RemoteCluster;
import com.dianping.midasx.world.cluster.core.ICluster;
import com.dianping.midasx.world.relation.Condition;
import com.dianping.midasx.world.relation.Relation;
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
    private static Table<String, ICluster<?>> clusters = new Table<String, ICluster<?>>();


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
