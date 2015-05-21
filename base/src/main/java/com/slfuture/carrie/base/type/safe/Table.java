package com.slfuture.carrie.base.type.safe;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 安全映射类
 */
public class Table<K, V> extends com.slfuture.carrie.base.type.Table<K, V> {
    /**
     * 构造函数
     */
    public Table() {
        map = new ConcurrentHashMap<K, V>();
    }
}
