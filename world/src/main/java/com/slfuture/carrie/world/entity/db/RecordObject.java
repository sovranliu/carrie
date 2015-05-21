package com.slfuture.carrie.world.entity.db;

import com.slfuture.carrie.base.type.Record;
import com.slfuture.carrie.base.type.core.IMapping;
import com.slfuture.carrie.world.IObject;

/**
 * 数据库映射对象
 */
public class RecordObject implements IObject, IMapping<String, Object> {
    /**
     * 记录对象
     */
    public Record record;
    /**
     * 簇对象
     */
    public RecordCluster cluster = null;


    /**
     * 构造函数
     */
    public RecordObject() { }

    /**
     * 构造函数
     *
     * @param record 记录对象
     * @param cluster 簇对象
     */
    public RecordObject(Record record, RecordCluster cluster) {
        this.record = record;
        this.cluster = cluster;
    }

    /**
     * 方法调用
     *
     * @param method 方法名称
     * @param parameters 参数列表
     * @return 返回值
     */
    public Object call(String method, Object... parameters) throws Exception {
        return cluster.call(this, method, parameters);
    }

    /**
     * 获取对象类型
     *
     * @return 对象类型
     */
    @Override
    public int type() {
        return IObject.TYPE_RECORD;
    }

    /**
     * 获取指定参数对应值
     *
     * @param key 参数
     * @return 值，若值不存在则返回null
     */
    @Override
    public Object get(String key) {
        return record.get(key);
    }
}
