package com.slfuture.carrie.world.relation.prepare;

import com.slfuture.carrie.world.logic.Agent;
import com.slfuture.carrie.world.relation.prepare.core.IPrepare;

/**
 * 对象适配器准备类
 */
public class AgentPrepare implements IPrepare {
    /**
     * 属性名
     */
    public String property = null;


    /**
     * 构造函数
     */
    public AgentPrepare() { }

    /**
     * 构造函数
     *
     * @param property 属性名称
     */
    public AgentPrepare(String property) {
        this.property = property;
    }

    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    @Override
    public Object filter(Object origin) {
        if(null == origin) {
            return null;
        }
        Agent agent = (Agent) origin;
        return agent.property(property);
    }

    /**
     * 转换为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return property;
    }

    /**
     * 克隆
     *
     * @return 克隆对象
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
