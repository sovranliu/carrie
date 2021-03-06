package com.slfuture.carrie.world.cluster;

import com.slfuture.carrie.base.model.Method;
import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.base.type.core.ITable;
import com.slfuture.carrie.world.invoker.RemoteInvoker;
import com.slfuture.carrie.world.relation.Condition;

/**
 * 远程簇
 */
public class RemoteCluster extends Cluster<ITable<String, Object>> {
    /**
     * 查找符合条件的对象
     *
     * @param condition 查找条件
     * @return 查找到的指定对象
     */
    public ITable<String, Object> find(Condition condition) {
        return RemoteInvoker.instance().find(name, condition);
    }

    /**
     * 查找符合条件的对象集
     *
     * @param condition 查找条件
     * @return 查找到的对象集
     */
    public ICollection<ITable<String, Object>> finds(Condition condition) {
        return RemoteInvoker.instance().finds(name, condition);
    }

    /**
     * 调用方法
     *
     * @param target 被调用对象
     * @param method 方法
     * @param args 参数列表
     * @return 调用结果
     */
    @Override
    public Object invoke(ITable<String, Object> target, Method method, Object...args) {
        return RemoteInvoker.instance().invoke(name, target.get(this.primaryKey), method, args);
    }
}
