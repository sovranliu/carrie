package com.dianping.midasx.world;

import com.dianping.midasx.base.type.Table;
import com.dianping.midasx.base.type.core.ISignedEdgedDigraph;

/**
 * 簇类
 */
public class Cluster extends Table<String, Relation> implements ISignedEdgedDigraph<String, Cluster, Relation> { 
    /**
     * 簇名称
     */
    public String name;
    /**
     * 管理器
     */
    public IManager<?> manager = null;
}
