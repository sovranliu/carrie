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
}
