package com.dianping.midasx.world.logic;

import com.dianping.midasx.base.model.Method;
import com.dianping.midasx.base.type.Record;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.IMapping;

/**
 * 对象适配器类
 */
public class Adapter {
    /**
     * 数据库对象
     */
    public final static int TYPE_DB = 1;
    /**
     * 本地对象
     */
    public final static int TYPE_LOCAL = 2;
    /**
     * 远程对象
     */
    public final static int TYPE_REMOTE = 3;


    /**
     * 簇名称
     */
    public String clusterName;
    /**
     * 适配对象类型
     */
    protected int type = 0;
    /**
     * 适配对象
     */
    protected Object target = null;


    /**
     * 构造函数
     *
     * @param record 数据库记录
     */
    public Adapter(Record record) {
        type = TYPE_DB;
        target = record;
    }

    /**
     * 构造函数
     *
     * @param object 本地对象
     */
    public Adapter(Object object) {
        type = TYPE_LOCAL;
        target = object;
    }

    /**
     * 构造函数
     *
     * @param properties 属性集
     */
    public Adapter(IMapping<String, Object> properties) {
        type = TYPE_REMOTE;
        target = properties;
    }

    /**
     * 获取适配对象类型
     *
     * @return 适配对象类型
     */
    public int type() {
        return this.type;
    }

    /**
     * 获取属性
     *
     * @return 属性值
     */
    public Object property(String property) {
        return null;
    }

    /**
     * 获取关系对象
     *
     * @param name 关系名称
     * @return 关系对象
     */
    public Adapter relative(String name) {
        return null;
    }

    /**
     * 获取关系对象列表
     *
     * @param name 关系名称
     * @return 关系对象集
     */
    public ICollection<Adapter> relatives(String name) {
        return null;
    }

    /**
     * 调用方法
     *
     * @param method 方法
     * @param args 参数列表
     * @return 方法值
     */
    public Object invoke(Method method, Object... args) {
        return null;
    }
}
