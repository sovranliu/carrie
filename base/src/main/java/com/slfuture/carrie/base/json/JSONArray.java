package com.slfuture.carrie.base.json;

import com.slfuture.carrie.base.json.core.IJSON;
import com.slfuture.carrie.base.type.List;

/**
 * JSON值数组类
 */
public class JSONArray extends List<IJSON> implements IJSON {
    /**
     * 获取对象类型
     *
     * @return 对象类型
     */
    @Override
    public int type() {
        return IJSON.JSON_TYPE_ARRAY;
    }

    /**
     * 将字符串转换为JSON数组
     *
     * @param string 字符串
     * @return JSON数组
     */
    public static JSONArray convert(String string) {
        return convert(new com.slfuture.carrie.base.json.thirdparty.JSONArray(string));
    }

    /**
     * 将第三方JSON数组转为JSON数组
     *
     * @param array 第三方JSON数组
     * @return JSON数组
     */
    static JSONArray convert(com.slfuture.carrie.base.json.thirdparty.JSONArray array) {
        JSONArray result = new JSONArray();
        for(int i = 0; i < array.length(); i++) {
            Object object = array.get(i);
            if(object instanceof Boolean) {
                result.add(new JSONBoolean((Boolean)object));
            }
            else if(object instanceof Integer) {
                result.add(new JSONNumber((Integer)object));
            }
            else if(object instanceof Long) {
                result.add(new JSONNumber((Long)object));
            }
            else if(object instanceof Double) {
                result.add(new JSONNumber((Double)object));
            }
            else if(object instanceof com.slfuture.carrie.base.json.thirdparty.JSONObject) {
                result.add(JSONObject.convert((com.slfuture.carrie.base.json.thirdparty.JSONObject) object));
            }
            else if(object instanceof com.slfuture.carrie.base.json.thirdparty.JSONArray) {
                result.add((IJSON)JSONArray.convert((com.slfuture.carrie.base.json.thirdparty.JSONArray) object));
            }
        }
        return result;
    }
}
