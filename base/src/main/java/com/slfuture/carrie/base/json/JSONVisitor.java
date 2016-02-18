package com.slfuture.carrie.base.json;

import com.slfuture.carrie.base.json.core.IJSON;
import com.slfuture.carrie.base.time.Date;
import com.slfuture.carrie.base.time.DateTime;
import com.slfuture.carrie.base.time.Time;
import com.slfuture.carrie.base.type.List;
import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.base.type.core.ILink;
import com.slfuture.carrie.base.type.core.IMixedMapping;
import com.slfuture.carrie.base.type.core.ITable;

import java.lang.reflect.Field;

/**
 * JSON 遍历器
 */
public class JSONVisitor implements IMixedMapping<String, Object> {
    /**
     * JSON对象
     */
    private IJSON value = null;


    /**
     * 构造函数
     */
    public JSONVisitor() { }

    /**
     * 构造函数
     *
     * @param value JSON
     */
    public JSONVisitor(IJSON value) {
        this.value = value;
    }

    /**
     * 转为字节
     *
     * @return 字节
     */
    public Boolean toBoolean() {
        if(false == (value instanceof JSONBoolean)) {
            return null;
        }
        return ((JSONBoolean) value).getValue();
    }

    /**
     * 转为字节
     *
     * @return 字节
     */
    public Byte toByte() {
        if(false == (value instanceof JSONNumber)) {
            return null;
        }
        return ((JSONNumber) value).byteValue();
    }

    /**
     * 转为短整型
     *
     * @return 短整型
     */
    public Short toShort() {
        if(false == (value instanceof JSONNumber)) {
            return null;
        }
        return ((JSONNumber) value).shortValue();
    }

    /**
     * 转为整型
     *
     * @return 整型
     */
    public Integer toInteger() {
        if(false == (value instanceof JSONNumber)) {
            return null;
        }
        return ((JSONNumber) value).intValue();
    }

    /**
     * 转为单精度
     *
     * @return 单精度
     */
    public Float toFloat() {
        if(false == (value instanceof JSONNumber)) {
            return null;
        }
        return ((JSONNumber) value).floatValue();
    }

    /**
     * 转为双精度
     *
     * @return 双精度
     */
    public Double toDouble() {
        if(false == (value instanceof JSONNumber)) {
            return null;
        }
        return ((JSONNumber) value).doubleValue();
    }

    /**
     * 获取字符值
     *
     * @param key 名
     * @return 字符值
     */
    public Character toCharacter(String key) {
        if(false == (value instanceof JSONString)) {
            return null;
        }
        return ((JSONString) value).getValue().charAt(0);
    }

    /**
     * 转为双精度
     *
     * @return 双精度
     */
    public String toString() {
        if(false == (value instanceof JSONString)) {
            return value.toString();
        }
        return ((JSONString) value).getValue();
    }

    /**
     * 获取子节点集合
     *
     * @return 子节点集合
     */
    public ICollection<JSONVisitor> toVisitors() {
        if(false == (value instanceof JSONArray)) {
            return null;
        }
        JSONArray array = (JSONArray) value;
        List<JSONVisitor> result = new List<JSONVisitor>();
        for(IJSON item : array) {
            result.add(new JSONVisitor(item));
        }
        return result;
    }

    /**
     * 获取属性节点映射
     *
     * @return 子节点集合
     */
    public ITable<String, JSONVisitor> toVisitorMap() {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        Table<String, JSONVisitor> result = new Table<String, JSONVisitor>();
        JSONObject object = (JSONObject) value;
        for(ILink<String, IJSON> link : object) {
            result.put(link.origin(), new JSONVisitor(link.destination()));
        }
        return result;
    }

    /**
     * 获取子节点
     *
     * @param key 名
     * @return 子节点
     */
    public JSONVisitor getVisitor(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target) {
            return null;
        }
        if(target instanceof JSONObject || target instanceof JSONArray) {
            return new JSONVisitor(target);
        }
        return null;
    }

    /**
     * 获取子节点
     *
     * @param key 名
     * @return 子节点
     */
    public ICollection<JSONVisitor> getVisitors(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target) {
            return null;
        }
        if(false == target instanceof JSONArray) {
            return null;
        }
        JSONArray array = (JSONArray) target;
        List<JSONVisitor> result = new List<JSONVisitor>();
        for(IJSON item : array) {
            result.add(new JSONVisitor(item));
        }
        return result;
    }

    /**
     * 获取布尔值
     *
     * @param key 名
     * @return 布尔值
     */
    @Override
    public Boolean getBoolean(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONBoolean)) {
            return null;
        }
        return ((JSONBoolean)target).getValue();
    }

    /**
     * 获取布尔值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 布尔值
     */
    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        if(false == (value instanceof JSONObject)) {
            return defaultValue;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONBoolean)) {
            return defaultValue;
        }
        return ((JSONBoolean)target).getValue();
    }

    /**
     * 获取字符值
     *
     * @param key 名
     * @return 字符值
     */
    @Override
    public Character getCharacter(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONString)) {
            return null;
        }
        return ((JSONString)target).getValue().charAt(0);
    }

    /**
     * 获取字符值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 字符值
     */
    @Override
    public char getCharacter(String key, char defaultValue) {
        if(false == (value instanceof JSONObject)) {
            return defaultValue;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONString)) {
            return defaultValue;
        }
        return ((JSONString)target).getValue().charAt(0);
    }

    /**
     * 获取字节值
     *
     * @param key 名
     * @return 字节值
     */
    @Override
    public Byte getByte(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return null;
        }
        return ((JSONNumber)target).byteValue();
    }

    /**
     * 获取字节值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 字节值
     */
    @Override
    public byte getByte(String key, byte defaultValue) {
        if(false == (value instanceof JSONObject)) {
            return defaultValue;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return defaultValue;
        }
        return ((JSONNumber)target).byteValue();
    }

    /**
     * 获取短整型值
     *
     * @param key 名
     * @return 短整型值
     */
    @Override
    public Short getShort(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return null;
        }
        return ((JSONNumber)target).shortValue();
    }

    /**
     * 获取短整型值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 短整型值
     */
    @Override
    public short getShort(String key, short defaultValue) {
        if(false == (value instanceof JSONObject)) {
            return defaultValue;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return defaultValue;
        }
        return ((JSONNumber)target).shortValue();
    }

    /**
     * 获取整型值
     *
     * @param key 名
     * @return 整型值
     */
    @Override
    public Integer getInteger(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return null;
        }
        return ((JSONNumber)target).intValue();
    }

    /**
     * 获取整型值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 整型值
     */
    @Override
    public int getInteger(String key, int defaultValue) {
        if(false == (value instanceof JSONObject)) {
            return defaultValue;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return defaultValue;
        }
        return ((JSONNumber)target).intValue();
    }

    /**
     * 获取长整型值
     *
     * @param key 名
     * @return 长整型值
     */
    @Override
    public Long getLong(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return null;
        }
        return ((JSONNumber)target).longValue();
    }

    /**
     * 获取长整型值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 长整型值
     */
    @Override
    public long getLong(String key, long defaultValue) {
        if(false == (value instanceof JSONObject)) {
            return defaultValue;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return defaultValue;
        }
        return ((JSONNumber)target).longValue();
    }

    /**
     * 获取单精度值
     *
     * @param key 名
     * @return 单精度值
     */
    @Override
    public Float getFloat(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return null;
        }
        return ((JSONNumber)target).floatValue();
    }

    /**
     * 获取单精度值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 单精度值
     */
    @Override
    public float getFloat(String key, float defaultValue) {
        if(false == (value instanceof JSONObject)) {
            return defaultValue;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return defaultValue;
        }
        return ((JSONNumber)target).floatValue();
    }

    /**
     * 获取双精度值
     *
     * @param key 名
     * @return 双精度值
     */
    @Override
    public Double getDouble(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return null;
        }
        return ((JSONNumber)target).doubleValue();
    }

    /**
     * 获取双精度值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 双精度值
     */
    @Override
    public double getDouble(String key, double defaultValue) {
        if(false == (value instanceof JSONObject)) {
            return defaultValue;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONNumber)) {
            return defaultValue;
        }
        return ((JSONNumber)target).doubleValue();
    }

    /**
     * 获取字符串
     *
     * @param key 名
     * @return 字符串
     */
    @Override
    public String getString(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        JSONObject object = (JSONObject) value;
        IJSON target = object.get(key);
        if(null == target || false == (target instanceof JSONString)) {
            return null;
        }
        return ((JSONString)target).getValue();
    }

    /**
     * 获取指定参数对应值
     *
     * @param key 参数
     * @return 值，若值不存在则返回null
     */
    @Override
    public Object get(String key) {
        if(false == (value instanceof JSONObject)) {
            return null;
        }
        return ((JSONObject) value).get(key);
    }

    /**
     * 构建指定类实例
     *
     * @param clazz 类
     * @return 类实例
     */
    public <T> T build(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T result = (T) clazz.newInstance();
        for(ILink<String, IJSON> link : (JSONObject) value) {
            try {
                for(Field field : clazz.getFields()) {
                    if(field.getName().equalsIgnoreCase(link.origin())) {
                        if(null == link.destination()) {
                            field.set(result, null);
                        }
                        else if(link.destination() instanceof JSONNumber) {
                            double d = ((JSONNumber) (link.destination())).doubleValue();
                            if(field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                                if(0 == d) {
                                    field.set(result, false);
                                }
                                else {
                                    field.set(result, true);
                                }
                            }
                            else if(field.getType().equals(byte.class) || field.getType().equals(Byte.class)) {
                                field.set(result, (byte) d);
                            }
                            else if(field.getType().equals(short.class) || field.getType().equals(Short.class)) {
                                field.set(result, (short) d);
                            }
                            else if(field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                                field.set(result, (int) d);
                            }
                            else if(field.getType().equals(long.class) || field.getType().equals(Long.class)) {
                                field.set(result, (long) d);
                            }
                            else if(field.getType().equals(float.class) || field.getType().equals(Float.class)) {
                                field.set(result, (float) d);
                            }
                            else {
                                field.set(result, d);
                            }
                        }
                        else if(link.destination() instanceof JSONBoolean) {
                            field.set(result, ((JSONBoolean) (link.destination())).getValue());
                        }
                        else if(link.destination() instanceof JSONString) {
                            if(field.getType().equals(DateTime.class)) {
                                field.set(result, DateTime.parse(((JSONString) link.destination()).getValue()));
                            }
                            else if(field.getType().equals(Date.class)) {
                                field.set(result, Date.parse(((JSONString) link.destination()).getValue()));
                            }
                            else if(field.getType().equals(Time.class)) {
                                field.set(result, Time.parse(((JSONString) link.destination()).getValue()));
                            }
                            else {
                                field.set(result, ((JSONString) (link.destination())).getValue());
                            }
                        }
                        field.set(result, link.destination());
                    }
                }
            }
            catch (Exception e) { }
        }
        return result;
    }

    /**
     * 赋值指定实例
     *
     * @param target 类实例
     */
    public <T> void build(T target) {
        for(ILink<String, IJSON> link : (JSONObject) value) {
            for(Field field : target.getClass().getDeclaredFields()) {
                if(field.getName().equalsIgnoreCase(link.origin())) {
                    try {
                        if(null == link.destination()) {
                            field.set(target, null);
                        }
                        else if(link.destination() instanceof JSONNumber) {
                            double d = ((JSONNumber) (link.destination())).doubleValue();
                            if(field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                                if(0 == d) {
                                    field.set(target, false);
                                }
                                else {
                                    field.set(target, true);
                                }
                            }
                            else if(field.getType().equals(byte.class) || field.getType().equals(Byte.class)) {
                                field.set(target, (byte) d);
                            }
                            else if(field.getType().equals(short.class) || field.getType().equals(Short.class)) {
                                field.set(target, (short) d);
                            }
                            else if(field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                                field.set(target, (int) d);
                            }
                            else if(field.getType().equals(long.class) || field.getType().equals(Long.class)) {
                                field.set(target, (long) d);
                            }
                            else if(field.getType().equals(float.class) || field.getType().equals(Float.class)) {
                                field.set(target, (float) d);
                            }
                            else {
                                field.set(target, d);
                            }
                        }
                        else if(link.destination() instanceof JSONBoolean) {
                            field.set(target, ((JSONBoolean) (link.destination())).getValue());
                        }
                        else if(link.destination() instanceof JSONString) {
                            if(field.getType().equals(DateTime.class)) {
                                field.set(target, DateTime.parse(((JSONString) link.destination()).getValue()));
                            }
                            else if(field.getType().equals(Date.class)) {
                                field.set(target, Date.parse(((JSONString) link.destination()).getValue()));
                            }
                            else if(field.getType().equals(Time.class)) {
                                field.set(target, Time.parse(((JSONString) link.destination()).getValue()));
                            }
                            else {
                                field.set(target, ((JSONString) (link.destination())).getValue());
                            }
                        }
                        field.set(target, link.destination());
                    }
                    catch (Exception e) { }
                }
            }
        }
    }
}
