package com.dianping.midasx.world.logic;

import com.dianping.midasx.base.model.Method;
import com.dianping.midasx.base.type.Record;
import com.dianping.midasx.base.type.Set;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.IMapping;
import com.dianping.midasx.world.World;
import com.dianping.midasx.world.cluster.DBCluster;
import com.dianping.midasx.world.cluster.LocalCluster;
import com.dianping.midasx.world.cluster.RemoteCluster;
import com.dianping.midasx.world.cluster.core.ICluster;
import com.dianping.midasx.world.relation.Condition;
import com.dianping.midasx.world.relation.Relation;

/**
 * 对象适配器类
 */
public class Adapter {
    /**
     * 数据库对象
     */
    public final static int TYPE_DB = 1;
    /**
     * 本地对象
     */
    public final static int TYPE_LOCAL = 2;
    /**
     * 远程对象
     */
    public final static int TYPE_REMOTE = 3;


    /**
     * 簇名称
     */
    public String clusterName;
    /**
     * 适配对象类型
     */
    protected int type = 0;
    /**
     * 适配对象
     */
    protected Object target = null;


    /**
     * 获取适配对象类型
     *
     * @return 适配对象类型
     */
    public int type() {
        return this.type;
    }

    /**
     * 获取属性
     *
     * @return 属性值
     */
    public Object property(String property) {
        switch(type) {
            case TYPE_DB: {
                Record record = (Record) target;
                return record.get(property);
            }
            case TYPE_LOCAL: {
                try {
                    return target.getClass().getDeclaredField(property).get(target);
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            case TYPE_REMOTE:{
                IMapping<String, Object> map = (IMapping<String, Object>) target;
                return map.get(property);
            }
        }
        return null;
    }

    /**
     * 获取关系对象
     *
     * @param name 关系名称
     * @return 关系对象
     */
    public Adapter relative(String name) {
        Relation relation = World.getCluster(clusterName).get(name);
        Condition condition = relation.deduce(this);
        ICluster<?> cluster = World.getCluster(relation.cluster);
        Adapter result = new Adapter();
        result.clusterName = relation.cluster;
        result.target = cluster.find(condition);
        if(cluster instanceof DBCluster) {
            result.type = TYPE_DB;
        }
        else if(cluster instanceof LocalCluster) {
            result.type = TYPE_LOCAL;
        }
        else if(cluster instanceof RemoteCluster) {
            result.type = TYPE_REMOTE;
        }
        return result;
    }

    /**
     * 获取关系对象列表
     *
     * @param name 关系名称
     * @return 关系对象集
     */
    public ICollection<Adapter> relatives(String name) {
        Relation relation = World.getCluster(clusterName).get(name);
        Condition condition = relation.deduce(this);
        ICluster<?> cluster = World.getCluster(relation.cluster);
        Set<Adapter> result = new Set<Adapter>();
        ICollection<Object> col =

        result.clusterName = relation.cluster;
        result.target = cluster.find(condition);
        if(cluster instanceof DBCluster) {
            result.type = TYPE_DB;
        }
        else if(cluster instanceof LocalCluster) {
            result.type = TYPE_LOCAL;
        }
        else if(cluster instanceof RemoteCluster) {
            result.type = TYPE_REMOTE;
        }
        return result;
    }

    /**
     * 调用方法
     *
     * @param method 方法
     * @param args 参数列表
     * @return 方法值
     */
    public Object invoke(Method method, Object... args) {
        return null;
    }
}
