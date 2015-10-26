package com.slfuture.carrie.world.relation.prepare;

import com.slfuture.carrie.world.IObject;
import com.slfuture.carrie.world.logic.Agent;
import com.slfuture.carrie.world.relation.prepare.core.IPrepare;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;

/**
 * 属性比较准备类
 */
public class PropertyPrepare implements IPrepare {
    /**
     * 日志对象
     */
    private static Logger logger = Logger.getLogger(PropertyPrepare.class);
    /**
     * 属性名称
     */
    public String property = null;


    /**
     * 构造函数
     */
    public PropertyPrepare() { }

    /**
     * 构造函数
     *
     * @param property 属性名称
     */
    public PropertyPrepare(String property) {
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
        if(origin.getClass().isAssignableFrom(Agent.class)) {
            Agent agent = (Agent) origin;
            return agent.property(property);
        }
        else if(IObject.class.isAssignableFrom(origin.getClass())) {
            IObject object = (IObject) origin;
            return object.property(property);
        }
        else {
            try {
                Field field = origin.getClass().getField(property);
                return field.get(origin);
            }
            catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * 克隆
     *
     * @return 克隆对象
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * 转化为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return property;
    }

    /**
     * 比较
     *
     * @param object 待比较对象
     * @return 比较结果
     */
    @Override
    public boolean equals(Object object) {
        if(null == object) {
            return false;
        }
        if(!object.getClass().isAssignableFrom(PropertyPrepare.class)) {
            return false;
        }
        PropertyPrepare prepare = (PropertyPrepare) object;
        if(null == property) {
            return null == prepare.property;
        }
        else {
            return property.equals(prepare.property);
        }
    }
}
