package com.dianping.midasx.world.condition;

import com.dianping.midasx.base.logic.CompareCondition;
import com.dianping.midasx.base.text.Text;
import com.dianping.midasx.base.time.Date;
import com.dianping.midasx.base.time.DateTime;
import com.dianping.midasx.base.time.Time;
import com.dianping.midasx.base.xml.core.IXMLNode;
import com.dianping.midasx.utility.db.DBFieldInfo;
import com.dianping.midasx.world.IObject;

import java.text.ParseException;

/**
 * 字段条件
 */
public class FieldCondition extends CompareCondition<IObject, Comparable<IObject>, FieldCondition> {
    /**
     * 构建字段条件
     *
     * @param conf 配置对象
     * @return 字段条件
     */
    public static FieldCondition build(IXMLNode conf) throws ParseException {
        FieldCondition condition = new FieldCondition();
        condition.compareType = conf.get("type");
        String dataType = conf.get("datatype");
        if(null == dataType) {
            dataType = Text.substring(conf.get("value"), "(", ")");
        }
        if(DBFieldInfo.DATATYPE_BYTE.equals(dataType)) {
            FieldComparator<Byte> field = new FieldComparator<Byte>();
            field.field = conf.get("field");
            field.value = Byte.valueOf(conf.get("value"));
            condition.value = field;
        }
        else if(DBFieldInfo.DATATYPE_SHORT.equals(dataType)) {
            FieldComparator<Short> field = new FieldComparator<Short>();
            field.field = conf.get("field");
            field.value = Short.valueOf(conf.get("value"));
            condition.value = field;
        }
        else if(DBFieldInfo.DATATYPE_INT.equals(dataType)) {
            FieldComparator<Integer> field = new FieldComparator<Integer>();
            field.field = conf.get("field");
            field.value = Integer.valueOf(conf.get("value"));
            condition.value = field;
        }
        else if(DBFieldInfo.DATATYPE_STRING.equals(dataType)) {
            FieldComparator<String> field = new FieldComparator<String>();
            field.field = conf.get("field");
            field.value = conf.get("value");
            condition.value = field;
        }
        else if(DBFieldInfo.DATATYPE_TIME.equals(dataType)) {
            FieldComparator<Time> field = new FieldComparator<Time>();
            field.field = conf.get("field");
            field.value = Time.parse(conf.get("value"));
            condition.value = field;
        }
        else if(DBFieldInfo.DATATYPE_DATE.equals(dataType)) {
            FieldComparator<Date> field = new FieldComparator<Date>();
            field.field = conf.get("field");
            field.value = Date.parse(conf.get("value"));
            condition.value = field;
        }
        else if(DBFieldInfo.DATATYPE_DATETIME.equals(dataType)) {
            FieldComparator<DateTime> field = new FieldComparator<DateTime>();
            field.field = conf.get("field");
            field.value = DateTime.parse(conf.get("value"));
            condition.value = field;
        }
        else if(DBFieldInfo.DATATYPE_TIMESTAMP.equals(dataType)) {
            FieldComparator<DateTime> field = new FieldComparator<DateTime>();
            field.field = conf.get("field");
            field.value = DateTime.parse(conf.get("value"));
            condition.value = field;
        }
        return condition;
    }
}
