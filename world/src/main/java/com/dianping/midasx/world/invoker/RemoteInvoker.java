package com.dianping.midasx.world.invoker;

import com.dianping.midasx.base.model.Identity;

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
     * @param method 方法名
     * @param parameters 参数
     * @return 返回结果
     */
    public Object call(String clusterName, Identity<Object> id, String method, Object...parameters) {
        return null;
    }
}
