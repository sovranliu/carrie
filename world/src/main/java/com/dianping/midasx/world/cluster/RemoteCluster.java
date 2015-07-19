package com.dianping.midasx.world.cluster;

import com.dianping.midasx.base.model.Method;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.IMapping;
import com.dianping.midasx.world.invoker.RemoteInvoker;
import com.dianping.midasx.world.relation.Condition;

/**
 * 远程簇
 */
public class RemoteCluster extends Cluster<IMapping<String, Object>> {
    /**
     * 查找符合条件的对象
     *
     * @param condition 查找条件
     * @return 查找到的指定对象
     */
    public IMapping<String, Object> find(Condition condition) {
        return RemoteInvoker.instance().find(name, condition);
    }

    /**
     * 查找符合条件的对象集
     *
     * @param condition 查找条件
     * @return 查找到的对象集
     */
    public ICollection<IMapping<String, Object>> finds(Condition condition) {
        return RemoteInvoker.instance().finds(name, condition);
    }

    /**
     * 调用方法
     *
     * @param id 被调用对象标志符
     * @param method 方法
     * @param args 参数列表
     * @return 调用结果
     */
    public Object invoke(Object id, Method method, Object... args) {
        return RemoteInvoker.instance().invoke(name, id, method, args);
    }
}
