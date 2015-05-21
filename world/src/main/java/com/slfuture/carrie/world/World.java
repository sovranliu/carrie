package com.slfuture.carrie.world;

import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.base.type.core.IMap;
import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.world.entity.db.RecordCluster;
import com.slfuture.carrie.world.entity.local.LocalCluster;
import com.slfuture.carrie.world.entity.remote.RemoteCluster;
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
     * 簇集合
     */
    public static IMap<String, Cluster> clusters = new Table<String, Cluster>();


    /**
     * 初始化
     *
     * @param conf 配置对象
     * @return 执行结果
     */
    public static boolean initialize(IXMLNode conf) {
        // 按配置加载接口类
        for(IXMLNode clusterNode : conf.visits("clusters/cluster")) {
            Cluster cluster = null;
            if(null != clusterNode.get("interface")) {
                cluster = LocalCluster.build(clusterNode);
                if(null == cluster) {
                    cluster = RemoteCluster.build(clusterNode);
                }
                if(null == cluster) {
                    logger.warn("java cluster can not build successfully\n" + clusterNode.toString());
                }
            }
            if(null == cluster && null != clusterNode.get("table")) {
                cluster = RecordCluster.build(clusterNode);
            }
            if(null == cluster) {
                logger.error("cluster build failed.\n" + clusterNode.toString());
            }
            clusters.put(cluster.name, cluster);
        }
        for(IXMLNode relationNode : conf.visits("relations/relation")) {
            Relation relation = null;
            try {
                relation = Relation.build(relationNode);
            }
            catch (ParseException e) {
                logger.error("call Relation.build() failed\n" + relationNode.toString(), e);
                return false;
            }
            relation.setLink(clusters.get(relationNode.get("self")), clusters.get(relationNode.get("target")));
            clusters.get(relationNode.get("self")).put(relationNode.get("name"), relation);
        }
        return true;
    }

    /**
     * 获取指定的簇
     *
     * @param name 簇名
     * @return 簇对象
     */
    public Cluster cluster(String name) {
        return clusters.get(name);
    }
}
