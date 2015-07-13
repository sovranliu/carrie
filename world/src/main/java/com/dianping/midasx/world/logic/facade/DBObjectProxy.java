package com.dianping.midasx.world.logic.facade;

import com.dianping.midasx.base.type.Record;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.world.World;
import com.dianping.midasx.world.logic.facade.core.IObjectProxy;

/**
 * 数据库代理对象类
 */
public class DBObjectProxy implements IObjectProxy {
    /**
     * 数据库名称
     */
    public String db;
    /**
     * 表名称
     */
    public String table;
    /**
     * 簇名称
     */
    public String cluster;
    /**
     * 数据库记录对象
     */
    public Record record = null;


    /**
     * 构造函数
     */
    public DBObjectProxy() { }

    /**
     * 构造函数
     *
     * @param db 数据库名称
     * @param table 表名称
     * @param cluster 簇名称
     * @param record 数据库记录
     */
    public DBObjectProxy(String db, String table, String cluster, Record record) {
        this.cluster = cluster;
        this.record = record;
    }

    /**
     * 获知指定名称的属性
     *
     * @param name 属性名
     * @return 属性值
     */
    @Override
    public Object property(String name) {
        return record.get(name);
    }

    /**
     * 获知指定名称的关系对象
     *
     * @param clazz 关系对象类
     * @param name  关系名
     * @return 关系对象
     */
    @Override
    public <T> T relative(Class<T> clazz, String name) {
        World.getCluster(cluster).get(name).deduce();

        return null;
    }

    /**
     * 获知指定名称的关系对象集
     *
     * @param clazz 关系对象类
     * @param name  关系名
     * @return 关系对象集
     */
    @Override
    public <T> ICollection<T> relatives(Class<T> clazz, String name) {
        return null;
    }

    /**
     * 调用指定方法名的方法
     *
     * @param name       属性名
     * @param classes    参数类列表
     * @param parameters 参数列表
     * @return 调用结果值
     */
    @Override
    public Object invoke(String name, Class<?>[] classes, Object[] parameters) {
        return null;
    }
}
