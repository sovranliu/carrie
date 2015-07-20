package com.dianping.midasx.world.logic;

import com.dianping.midasx.base.model.Method;
import com.dianping.midasx.base.type.Record;
import com.dianping.midasx.base.type.Set;
import com.dianping.midasx.base.type.Table;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.IMapping;
import com.dianping.midasx.base.type.core.ITable;
import com.dianping.midasx.world.World;
import com.dianping.midasx.world.annotation.Property;
import com.dianping.midasx.world.cluster.Cluster;
import com.dianping.midasx.world.cluster.DBCluster;
import com.dianping.midasx.world.cluster.LocalCluster;
import com.dianping.midasx.world.cluster.RemoteCluster;
import com.dianping.midasx.world.cluster.core.ICluster;
import com.dianping.midasx.world.relation.Condition;
import com.dianping.midasx.world.relation.Relation;

import java.lang.reflect.Field;

/**
 * 对象代理类
 */
public class Agent {
    /**
     * 未知对象类型
     */
    public final static int TYPE_UNKNOWN = 0;
    /**
     * 数据库对象类型
     */
    public final static int TYPE_DB = 1;
    /**
     * 本地对象类型
     */
    public final static int TYPE_LOCAL = 2;
    /**
     * 远程对象类型
     */
    public final static int TYPE_REMOTE = 3;


    /**
     * 簇名称
     */
    public String clusterName;
    /**
     * 代理对象类型
     */
    protected int type = TYPE_UNKNOWN;
    /**
     * 代理对象
     */
    public Object target = null;


    /**
     * 构造函数
     */
    public Agent() { }

    /**
     * 构造函数
     *
     * @param clusterName 簇名称
     * @param type 代理对象类型
     * @param target 代理对象
     */
    public Agent(String clusterName, int type, Object target) {
        this.clusterName = clusterName;
        this.type = type;
        this.target = target;
    }

    /**
     * 构造函数
     *
     * @param cluster 簇对象
     * @param target 代理对象
     */
    public Agent(Cluster<?> cluster, Object target) {
        this.clusterName = cluster.name;
        if(cluster instanceof DBCluster) {
            this.type = TYPE_DB;
        }
        else if(cluster instanceof LocalCluster) {
            this.type = TYPE_LOCAL;
        }
        else if(cluster instanceof RemoteCluster) {
            this.type = TYPE_REMOTE;
        }
        this.target = target;
    }

    /**
     * 获取适配对象类型
     *
     * @return 适配对象类型
     */
    public int type() {
        return this.type;
    }

    /**
     * 获取标志符
     *
     * @return 标志符
     */
    public Object id() {
        Cluster<?> cluster = (Cluster<?>)(World.getCluster(clusterName));
        return property(cluster.primaryKey);
    }

    /**
     * 获取指定名称的属性值
     *
     * @param name 属性名
     * @return 属性值
     */
    public Object property(String name) {
        switch(type) {
            case TYPE_DB: {
                Record record = (Record) target;
                return record.get(name);
            }
            case TYPE_LOCAL: {
                try {
                    return target.getClass().getDeclaredField(name).get(target);
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            case TYPE_REMOTE:{
                IMapping<String, Object> map = (IMapping<String, Object>) target;
                return map.get(name);
            }
            default: {
                throw new RuntimeException("unknown agent type:" + type);
            }
        }
    }

    /**
     * 获取属性表
     *
     * @return 属性表
     */
    public ITable<String, Object> properties() {
        switch(type) {
            case TYPE_DB: {
                return (Record) target;
            }
            case TYPE_LOCAL: {
                try {
                    Table<String, Object> result = new Table<String, Object>();
                    for(Field field : target.getClass().getDeclaredFields()) {
                        Property property = field.getAnnotation(Property.class);
                        if(null != property) {
                            if("".equals(property.name())) {
                                result.put(field.getName(), field.get(target));
                            }
                            else {
                                result.put(property.name(), field.get(target));
                            }
                        }
                    }
                    return result;
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            case TYPE_REMOTE:{
                return (ITable<String, Object>) target;
            }
            default: {
                throw new RuntimeException("unknown agent type:" + type);
            }
        }
    }

    /**
     * 获取关系对象
     *
     * @param name 关系名称
     * @return 关系对象
     */
    public Agent relative(String name) {
        Relation relation = World.getCluster(clusterName).get(name);
        Condition condition = relation.deduce(this);
        ICluster<?> cluster = World.getCluster(relation.cluster);
        int agentType = TYPE_UNKNOWN;
        if(cluster instanceof DBCluster) {
            agentType = TYPE_DB;
        }
        else if(cluster instanceof LocalCluster) {
            agentType = TYPE_LOCAL;
        }
        else if(cluster instanceof RemoteCluster) {
            agentType = TYPE_REMOTE;
        }
        return new Agent(relation.cluster, agentType, cluster.find(condition));
    }

    /**
     * 获取关系对象列表
     *
     * @param name 关系名称
     * @return 关系对象集
     */
    public ICollection<Agent> relatives(String name) {
        Relation relation = World.getCluster(clusterName).get(name);
        Condition condition = relation.deduce(this);
        ICluster<?> cluster = World.getCluster(relation.cluster);
        Set<Agent> result = new Set<Agent>();
        ICollection<?> targets = cluster.finds(condition);
        for(Object target : targets) {
            int agentType = TYPE_UNKNOWN;
            if(cluster instanceof DBCluster) {
                agentType = TYPE_DB;
            }
            else if(cluster instanceof LocalCluster) {
                agentType = TYPE_LOCAL;
            }
            else if(cluster instanceof RemoteCluster) {
                agentType = TYPE_REMOTE;
            }
            result.add(new Agent(relation.cluster, agentType, target));
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
        ICluster<?> cluster = World.getCluster(clusterName);
        if(cluster instanceof DBCluster) {
            return ((DBCluster) cluster).invoke((Record) target, method, args);
        }
        else if(cluster instanceof LocalCluster) {
            return ((LocalCluster) cluster).invoke(target, method, args);
        }
        else if(cluster instanceof RemoteCluster) {
            return ((RemoteCluster) cluster).invoke((ITable<String, Object>) target, method, args);
        }
        throw new RuntimeException("unknown agent type:" + type);
    }
}
