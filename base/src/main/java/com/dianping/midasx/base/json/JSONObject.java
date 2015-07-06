package com.dianping.midasx.base.json;

import com.dianping.midasx.base.json.core.IJSON;
import com.dianping.midasx.base.time.Date;
import com.dianping.midasx.base.type.Table;
import com.dianping.midasx.base.type.core.ILink;

/**
 * JSON值对象类
 */
public class JSONObject extends Table<String, IJSON> implements IJSON {

    /**
     * 获取对象类型
     *
     * @return 对象类型
     */
    @Override
    public int type() {
        return IJSON.JSON_TYPE_OBJECT;
    }

    /**
     * 转为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        boolean sentry = true;
        for (ILink<String, IJSON> link : this) {
            if (sentry) {
                sentry = false;
            }
            else {
                builder.append(",");
            }
            builder.append("\"");
            builder.append(link.origin());
            builder.append("\":");
            builder.append(link.destination());
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     * 将字符串转换为JSON对象
     *
     * @param jsonText 字符串
     * @return JSON对象
     */
    public static JSONObject convert(String jsonText) {
        return JSONObject.convert(new com.dianping.midasx.base.json.thirdparty.JSONObject(jsonText));
    }

    /**
     * 将表转换为JSON对象
     *
     * @param table 表
     * @return JSON对象
     */
    public static <T> JSONObject convert(Table<String, T> table) {
        JSONObject result = new JSONObject();
        for(ILink<String, T> link : table) {
            if(null == link.destination()) {
                result.put(link.origin(), null);
                continue;
            }
            if(link.destination() instanceof Boolean) {
                result.put(link.origin(), new JSONBoolean((Boolean) link.destination()));
            }
            else if(link.destination() instanceof Number) {
                result.put(link.origin(), new JSONNumber((Double) link.destination()));
            }
            else if(link.destination() instanceof String) {
                result.put(link.origin(), new JSONString((String) link.destination()));
            }
            else if(link.destination() instanceof Date) {
                result.put(link.origin(), new JSONString(((Date) link.destination()).toString()));
            }
            else if(link.destination() instanceof JSONObject) {
                result.put(link.origin(), (JSONObject) link.destination());
            }
            else {
                result.put(link.origin(), new JSONString(link.destination().toString()));
            }
        }
        return result;
    }

    /**
     * 将字符串转换为JSON对象
     *
     * @param jsonObject JSON对象
     * @return JSON对象
     */
    static JSONObject convert(com.dianping.midasx.base.json.thirdparty.JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        for(String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if(value instanceof Boolean) {
                result.put(key, new JSONBoolean((Boolean)value));
            }
            else if(value instanceof Integer) {
                result.put(key, new JSONNumber((Integer)value));
            }
            else if(value instanceof Long) {
                result.put(key, new JSONNumber((Long)value));
            }
            else if(value instanceof Double) {
                result.put(key, new JSONNumber((Double)value));
            }
			else if(value instanceof String) {
                result.put(key, new JSONString((String)value));
            }
            else if(value instanceof com.dianping.midasx.base.json.thirdparty.JSONObject) {
                result.put(key, JSONObject.convert((com.dianping.midasx.base.json.thirdparty.JSONObject)value));
            }
            else if(value instanceof com.dianping.midasx.base.json.thirdparty.JSONArray) {
                result.put(key, JSONArray.convert((com.dianping.midasx.base.json.thirdparty.JSONArray)value));
            }
        }
        return result;
    }
}
