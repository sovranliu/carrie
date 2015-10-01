package com.slfuture.carrie.utility.cache.core;

/**
 * 有时限的缓存
 */
public interface ITimeLimitedCache<K, V> extends ICache<K, V> {
    /**
     * 超时回调
     *
     * @param key 键
     * @return 值
     */
    public V onExpire(K key);
}
