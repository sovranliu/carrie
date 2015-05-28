package com.slfuture.carrie.base.type;

import com.slfuture.carrie.base.type.core.IMixedMapping;

/**
 * 字符串混合映射抽象类
 */
public abstract class StringMixedMapping<K> implements IMixedMapping<K, String> {
    /**
     * 获取布尔值
     *
     * @param key 名
     * @return 布尔值
     */
    @Override
    public Boolean getBoolean(K key) {
        return Boolean.valueOf(get(key));
    }

    /**
     * 获取布尔值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 布尔值
     */
    @Override
    public boolean getBoolean(K key, boolean defaultValue) {
        Boolean result = getBoolean(key);
        if(null == result) {
            return defaultValue;
        }
        return result;
    }

    /**
     * 获取字符值
     *
     * @param key 名
     * @return 字符值
     */
    @Override
    public Character getCharacter(K key) {
        return get(key).charAt(0);
    }

    /**
     * 获取字符值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 字符值
     */
    @Override
    public char getCharacter(K key, char defaultValue) {
        Character result = getCharacter(key);
        if(null == result) {
            return defaultValue;
        }
        return result;
    }

    /**
     * 获取字节值
     *
     * @param key 名
     * @return 字节值
     */
    @Override
    public Byte getByte(K key) {
        return Byte.valueOf(get(key));
    }

    /**
     * 获取字节值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 字节值
     */
    @Override
    public byte getByte(K key, byte defaultValue) {
        Byte result = getByte(key);
        if(null == result) {
            return defaultValue;
        }
        return result;
    }

    /**
     * 获取短整型值
     *
     * @param key 名
     * @return 短整型值
     */
    @Override
    public Short getShort(K key) {
        return Short.valueOf(get(key));
    }

    /**
     * 获取短整型值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 短整型值
     */
    @Override
    public short getShort(K key, short defaultValue) {
        Short result = getShort(key);
        if(null == result) {
            return defaultValue;
        }
        return result;
    }

    /**
     * 获取整型值
     *
     * @param key 名
     * @return 整型值
     */
    @Override
    public Integer getInteger(K key) {
        return Integer.valueOf(get(key));
    }

    /**
     * 获取整型值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 整型值
     */
    @Override
    public int getInteger(K key, int defaultValue) {
        Integer result = getInteger(key);
        if(null == result) {
            return defaultValue;
        }
        return result;
    }

    /**
     * 获取长整型值
     *
     * @param key 名
     * @return 长整型值
     */
    @Override
    public Long getLong(K key) {
        return Long.valueOf(get(key));
    }

    /**
     * 获取长整型值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 长整型值
     */
    @Override
    public long getLong(K key, long defaultValue) {
        Long result = getLong(key);
        if(null == result) {
            return defaultValue;
        }
        return result;
    }

    /**
     * 获取单精度值
     *
     * @param key 名
     * @return 单精度值
     */
    @Override
    public Float getFloat(K key) {
        return Float.valueOf(get(key));
    }

    /**
     * 获取单精度值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 单精度值
     */
    @Override
    public float getFloat(K key, float defaultValue) {
        Float result = getFloat(key);
        if(null == result) {
            return defaultValue;
        }
        return result;
    }

    /**
     * 获取双精度值
     *
     * @param key 名
     * @return 双精度值
     */
    @Override
    public Double getDouble(K key) {
        return Double.valueOf(get(key));
    }

    /**
     * 获取双精度值
     *
     * @param key          名
     * @param defaultValue 默认值
     * @return 双精度值
     */
    @Override
    public double getDouble(K key, double defaultValue) {
        Double result = getDouble(key);
        if(null == result) {
            return defaultValue;
        }
        return result;
    }
}
