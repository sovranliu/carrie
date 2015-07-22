package com.slfuture.carrie.world.relation.prepare;

import com.slfuture.carrie.world.relation.prepare.core.IPrepare;
import org.apache.log4j.Logger;

/**
 * 属性准备类
 */
public class PropertyPrepare implements IPrepare {
    /**
     * 日志对象
     */
    private static Logger logger = Logger.getLogger(PropertyPrepare.class);
    /**
     * 属性名称
     */
    public String property;


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
        return null;
    }

    /**
     * 克隆
     *
     * @return 克隆对象
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        }
        catch(CloneNotSupportedException ex) {
            return null;
        }
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
}
