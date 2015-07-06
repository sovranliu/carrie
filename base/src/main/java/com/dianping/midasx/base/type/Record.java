package com.dianping.midasx.base.type;

import com.dianping.midasx.base.time.Date;
import com.dianping.midasx.base.type.core.ILink;

import java.util.LinkedHashMap;

/**
 * 记录类
 */
public class Record extends MixedTable<String, Object> {
    /**
     * 获取字符串字段值
     *
     * @param key 字段名
     * @return 字符串值
     */
    public String getString(String key) {
        Object ret = this.get(key);
        if(null == ret) {
            return null;
        }
        return (String)ret;
    }

    /**
     * 获取日期字段值
     *
     * @param key 字段名
     * @return 日期值
     */
    public Date getDate(String key) {
        Object ret = this.get(key);
        if(null == ret) {
            return null;
        }
        return Date.parse((java.util.Date) ret);
    }

    /**
     * 重载比较函数
     *
     * @param object 被比较对象
     * @return 记录相等返回true，否则返回false
     */
    @Override
    public boolean equals(Object object) {
        if(!(object instanceof Record)) {
            return super.equals(object);
        }
        Record record = (Record) object;
        for(ILink<String, Object> link : this) {
            if(!link.destination().equals(record.get(link.origin()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转化为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        boolean sentry = true;
        for (ILink<String, Object> link : this) {
            if (sentry) {
                sentry = false;
            }
            else {
                builder.append(",");
            }
            builder.append("\"");
            builder.append(link.origin());
            builder.append("\":");
            if(null == link.destination()) {
                builder.append("null");
            }
            else if(link.destination() instanceof Boolean) {
                builder.append(link.destination());
            }
            else if(link.destination() instanceof Number) {
                builder.append(link.destination());
            }
            else if(link.destination() instanceof String) {
                builder.append("\"");
                builder.append(link.destination());
                builder.append("\"");
            }
            else if(link.destination() instanceof Date) {
                builder.append("\"");
                builder.append(((Date) link.destination()).toString());
                builder.append("\"");
            }
            else {
                builder.append("\"");
                builder.append(link.destination());
                builder.append("\"");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     * 初始化
     */
    @Override
    public void onNew() {
        map = new LinkedHashMap<String, Object>();
    }
}
