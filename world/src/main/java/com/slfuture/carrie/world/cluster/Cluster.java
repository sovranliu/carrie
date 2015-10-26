package com.slfuture.carrie.world.cluster;

import com.slfuture.carrie.base.type.Set;
import com.slfuture.carrie.base.type.safe.Table;
import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.world.cluster.core.ICluster;
import com.slfuture.carrie.world.event.EventPipe;
import com.slfuture.carrie.world.logic.Agent;
import com.slfuture.carrie.world.relation.Condition;
import com.slfuture.carrie.world.relation.Relation;
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
     * 捕捉者
     */
    public Table<String, ICollection<EventPipe>> catchers = new Table<String, ICollection<EventPipe>>();
    /**
     * 簇名称
     */
    public String name;
    /**
     * 主键
     */
    public String primaryKey;


    /**
     * 获取簇名称
     *
     * @return 簇名称
     */
    @Override
    public String name() {
        return this.name;
    }

    /**
     * 获取主键
     *
     * @return 主键
     */
    @Override
    public String primaryKey() {
        return this.primaryKey;
    }


    /**
     * 查找符合条件的代理
     *
     * @param condition 查找条件
     * @return 查找到的指定代理
     */
    @Override
    public Agent findAgent(Condition condition) {
        Object object = this.find(condition);
        if(null == object) {
            return null;
        }
        return new Agent(this, object);
    }

    /**
     * 查找符合条件的代理集
     *
     * @param condition 查找条件
     * @return 查找到的代理集
     */
    @Override
    public ICollection<Agent> findAgents(Condition condition) {
        Set<Agent> result = new Set<Agent>();
        for(Object object : this.finds(condition)) {
            result.add(new Agent(this, object));
        }
        return result;
    }
}
