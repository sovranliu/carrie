package com.dianping.midasx.world.invoker;

import com.dianping.midasx.base.model.Method;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.ITable;
import com.dianping.midasx.world.relation.Condition;

/**
 * 远程调用
 */
public class RemoteInvoker {
    /**
     * 单件实例
     */
    private static RemoteInvoker instance = null;


    /**
     * 隐藏构造函数
     */
    private RemoteInvoker() {}

    /**
     * 获取实例对象
     *
     * @return 实例对象
     */
    public static RemoteInvoker instance() {
        if(null == instance) {
            synchronized (RemoteInvoker.class) {
                if(null == instance) {
                    instance = new RemoteInvoker();
                }
            }
        }
        return instance;
    }

    /**
     * 远程调用
     *
     * @param clusterName 簇名称
     * @param id 对象标志符
     * @param method 方法
     * @param parameters 参数
     * @return 返回结果
     */
    public Object invoke(String clusterName, Object id, Method method, Object...parameters) {
        return null;
    }

    /**
     * 根据条件查找指定对象属性集
     *
     * @param clusterName 簇名称
     * @param condition 查找条件
     * @return 对象属性集
     */
    public ITable<String, Object> find(String clusterName, Condition condition) {
        return null;
    }

    /**
     * 根据条件查找指定对象属性集
     *
     * @param clusterName 簇名称
     * @param condition 查找条件
     * @return 对象属性集
     */
    public ICollection<ITable<String, Object>> finds(String clusterName, Condition condition) {
        return null;
    }
}
