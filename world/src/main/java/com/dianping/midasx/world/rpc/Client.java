package com.dianping.midasx.world.rpc;

import com.dianping.midasx.base.model.GlobeIdentity;
/**
 * RPC客户端类
 */
public class Client {
    /**
     * 构造函数
     */
    public Client() {
    }

    /**
     * 远程调用
     *
     * @param id 被调用对象全局标志
     * @param method 调用方法
     * @param parameters 实参列表
     * @return 调用返回值
     */
    public Object invoke(GlobeIdentity<Object> id, Method method, Object...parameters) {
        return null;
    }
}
