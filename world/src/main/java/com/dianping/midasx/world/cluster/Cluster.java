package com.dianping.midasx.world.cluster;

import com.dianping.midasx.base.type.Table;
import com.dianping.midasx.world.cluster.core.ICluster;
import com.dianping.midasx.world.relation.Relation;
import org.apache.log4j.Logger;

/**
 * 簇抽象类
 */
public abstract class Cluster<T> extends Table<String, Relation> implements ICluster<T> {
    /**
     * 日志对象
     */
    protected static Logger logger = Logger.getLogger(Cluster.class);
    /**
     * 簇名称
     */
    public String name;
    /**
     * 主键
     */
    public String primaryKey;
}
