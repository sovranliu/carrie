package com.slfuture.carrie.utility.cache;

import com.slfuture.carrie.base.type.core.IMapping;
import com.slfuture.carrie.base.type.core.ITable;
import com.slfuture.carrie.base.type.safe.Table;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 周期性缓存
 */
public abstract class IntervalCache<K, V> implements IMapping<K, V> {
    /**
     * 周期
     */
    private long interval = 10 * 1000;
    /**
     * 表
     */
    protected Table<K, V> map = new Table<K, V>();
    /**
     * 定时器
     */
    private Timer timer = new Timer();


    /**
     * 构造函数
     *
     * @param interval 刷新时长
     */
    public IntervalCache(long interval) {
        this.interval = interval;
        if(!onFresh(map)) {
            throw new IllegalStateException();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Table<K, V> newMap = new Table<K, V>();
                if(!onFresh(newMap)) {
                    return;
                }
                IntervalCache.this.map = newMap;
            }
        }, interval, interval);
    }

    /**
     * 获取指定键对应的值
     *
     * @param key 键
     * @return 值
     */
    @Override
    public V get(K key) {
        return map.get(key);
    }

    /**
     * 关闭
     */
    public void close() {
        if(null == timer) {
            return;
        }
        try {
            timer.cancel();
        }
        catch (Exception ex) { }
        timer = null;
    }

    /**
     * 刷新回调
     *
     * @param map 待刷新缓存
     * @return 是否刷新成功
     */
    public abstract boolean onFresh(ITable<K, V> map);
}
