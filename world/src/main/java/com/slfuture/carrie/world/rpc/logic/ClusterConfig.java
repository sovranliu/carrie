package com.slfuture.carrie.world.rpc.logic;

import com.slfuture.carrie.base.io.net.NetEntry;
import com.slfuture.carrie.base.type.Set;
import com.slfuture.carrie.base.type.core.ISet;

/**
 * 簇信息
 */
public class ClusterConfig {
    /**
     * 机器信息
     */
    public ISet<NetEntry> machines = new Set<NetEntry>();
    /**
     * 支持的簇名称
     */
    public ISet<String> clusterNames = new Set<String>();
}
