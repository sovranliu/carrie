package com.slfuture.carrie.utility.cache.core;

import com.slfuture.carrie.base.type.core.IMapping;

/**
 * 缓存接口
 */
public interface ICache<K, V> extends IMapping<K, V> {
    /**
     * 穿透回调
     *
     * @param key 键
     * @return 值
     */
    public V onMiss(K key);

    /**
     * 超时回调
     *
     * @param key 键
     * @return 值
     */
    public V onExpire(K key);
}
