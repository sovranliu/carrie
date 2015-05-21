package com.slfuture.carrie.world;

import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.base.type.core.ISignedEdgedDigraph;

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
