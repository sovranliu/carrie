package com.slfuture.carrie.utility.cache;

import com.slfuture.carrie.base.time.DateTime;
import com.slfuture.carrie.utility.cache.core.ITimeLimitedCache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存型缓存
 */
public abstract class SimpleCache<K, V> implements ITimeLimitedCache<K, V> {
    /**
     * 值结构体
     */
    public class Pair {
        /**
         * 值对象
         */
        public V value;
        /**
         * 超时时间
         */
        public double expireTime = 0;


        /**
         * 构造函数
         */
        public Pair() { }

        /**
         * 构造函数
         */
        public Pair(V value) {
            this.value = value;
            expireTime = DateTime.now().toLong();
        }
    }


    /**
     * 表
     */
    private ConcurrentHashMap<K, Pair> map = new ConcurrentHashMap<K, Pair>();
    /**
     * 有效时长
     */
    private long duration = 0;


    /**
     * 构造函数
     */
    public SimpleCache() { }
    public SimpleCache(long duration) {
        this.duration = duration;
    }


    /**
     * 获取有效时长
     *
     * @return 有效时长
     */
    public long duration() {
        return this.duration;
    }

    /**
     * 设置有效时长
     *
     * @param duration 有效时长
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * 获取指定键对应的值
     *
     * @param key 键
     * @return 值
     */
    @Override
    public V get(K key) {
        Pair pir = map.get(key);
        if(null == pir) {
            V value = onMiss(key);
            if(null == value) {
                return null;
            }
            map.put(key, new Pair(value));
            return value;
        }
        if(duration > 0) {
            if(pir.expireTime + duration > DateTime.now().toLong()) {
                return pir.value;
            }
            V value = onExpire(key);
            if(null == value) {
                return null;
            }
            map.put(key, new Pair(value));
        }
        return pir.value;
    }
}
