package com.dianping.midasx.world.relation.prepare;

import com.dianping.midasx.world.relation.prepare.core.IPrepare;
import org.apache.log4j.Logger;

/**
 * 字段准备类
 */
public class FieldPrepare implements IPrepare {
    /**
     * 日志对象
     */
    private static Logger logger = Logger.getLogger(FieldPrepare.class);
    /**
     * 字段名称
     */
    public String field;


    /**
     * 构造函数
     */
    public FieldPrepare() { }

    /**
     * 构造函数
     *
     * @param field 字段名称
     */
    public FieldPrepare(String field) {
        this.field = field;
    }

    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    @Override
    public Object filter(Object origin) {
        try {
            return origin.getClass().getDeclaredMethod(field).invoke(origin);
        }
        catch (Exception ex) {
            logger.error("FieldPrepare.filter(" + origin + ") execute failed", ex);
        }
        return null;
    }

    /**
     * 转化为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return field;
    }

    /**
     * 克隆
     *
     * @return 克隆对象
     */
    @Override
    public IPrepare copy() {
        try {
            return (IPrepare) super.clone();
        }
        catch(CloneNotSupportedException ex) {
            return null;
        }
    }
}
