package com.dianping.midasx.world.condition;

import com.dianping.midasx.base.logic.CompareCondition;
import com.dianping.midasx.base.time.Date;
import com.dianping.midasx.base.time.DateTime;
import com.dianping.midasx.base.time.Time;
import com.dianping.midasx.base.type.BooleanMappingDigraph;
import com.dianping.midasx.base.type.core.IMapping;
import com.dianping.midasx.base.xml.core.IXMLNode;
import com.dianping.midasx.utility.db.DBFieldInfo;
import com.dianping.midasx.world.IObject;
import com.dianping.midasx.world.facade.FacadeObject;

import java.text.ParseException;

/**
 * 关联条件
 */
public class RelatedCondition extends BooleanMappingDigraph<RelatedCondition> {
    /**
     * 关系
     */
    public FieldRelation<?> relative;
    /**
     * 线程对象
     */
    private ThreadLocal<IObject> threadTarget = new ThreadLocal<IObject>();


    /**
     * 推演
     *
     * @param origin 源对象
     * @return 目标对象的条件
     */
    public FieldCondition deduce(final IObject origin) {
        return this.<FieldCondition>clone(new IMapping<RelatedCondition, FieldCondition>() {
            @Override
            public FieldCondition get(RelatedCondition key) {
                FieldCondition condition = new FieldCondition();
                condition.value = relative.deduce(origin);
                condition.compareType = CompareCondition.COMPARETYPE_EQUAL;
                return condition;
            }
        });
    }

    /**
     * 转换为字符串
     *
     * @param target 本地对象
     * @param grammar 语法
     * @return 字符串
     */
    public String toString(IObject target, int grammar) {
        threadTarget.set(target);
        String result = toString(grammar);
        threadTarget.remove();
        return result;
    }

    /**
     * 转化为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        IObject target = threadTarget.get();
        if(null == target) {
            return relative.toString();
        }
        else {
            return relative.remote + " = " + FacadeObject.field(target, relative.local);
        }
    }


    /**
     * 构建关系条件
     *
     * @param conf 配置对象
     * @return 关系条件
     */
    public static RelatedCondition build(IXMLNode conf) throws ParseException {
        RelatedCondition condition = new RelatedCondition();
        String dataType = conf.get("datatype");
        if(null == dataType) {
            return null;
        }
        if(DBFieldInfo.DATATYPE_BYTE.equals(dataType)) {
            FieldRelation<Byte> relative = new FieldRelation<Byte>();
            relative.local = conf.get("field");
            relative.remote = conf.get("value");
            condition.relative = relative;
        }
        else if(DBFieldInfo.DATATYPE_SHORT.equals(dataType)) {
            FieldRelation<Short> relative = new FieldRelation<Short>();
            relative.local = conf.get("field");
            relative.remote = conf.get("value");
            condition.relative = relative;
        }
        else if(DBFieldInfo.DATATYPE_INT.equals(dataType)) {
            FieldRelation<Integer> relative = new FieldRelation<Integer>();
            relative.local = conf.get("field");
            relative.remote = conf.get("value");
            condition.relative = relative;
        }
        else if(DBFieldInfo.DATATYPE_STRING.equals(dataType)) {
            FieldRelation<String> relative = new FieldRelation<String>();
            relative.local = conf.get("field");
            relative.remote = conf.get("value");
            condition.relative = relative;
        }
        else if(DBFieldInfo.DATATYPE_TIME.equals(dataType)) {
            FieldRelation<Time> relative = new FieldRelation<Time>();
            relative.local = conf.get("field");
            relative.remote = conf.get("value");
            condition.relative = relative;
        }
        else if(DBFieldInfo.DATATYPE_DATE.equals(dataType)) {
            FieldRelation<Date> relative = new FieldRelation<Date>();
            relative.local = conf.get("field");
            relative.remote = conf.get("value");
            condition.relative = relative;
        }
        else if(DBFieldInfo.DATATYPE_DATETIME.equals(dataType)) {
            FieldRelation<DateTime> relative = new FieldRelation<DateTime>();
            relative.local = conf.get("field");
            relative.remote = conf.get("value");
            condition.relative = relative;
        }
        else if(DBFieldInfo.DATATYPE_TIMESTAMP.equals(dataType)) {
            FieldRelation<DateTime> relative = new FieldRelation<DateTime>();
            relative.local = conf.get("field");
            relative.remote = conf.get("value");
            condition.relative = relative;
        }
        return condition;
    }
}
