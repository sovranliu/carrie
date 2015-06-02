package com.slfuture.carrie.world.rpc;

import com.slfuture.carrie.base.model.GlobeIdentity;
import com.slfuture.carrie.utility.config.Configuration;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.utility.config.ConfigWatcher;
import com.slfuture.carrie.utility.config.core.IRootConfig;

/**
 * RPC客户端类
 */
public class Client {
    /**
     * 网络配置监视器类
     */
    public static class NetConfigWatcher implements ConfigWatcher {
        /**
         * 配置变动回调
         *
         * @param conf 配置对象
         */
        @Override
        public void onChanged(IConfig conf) {

        }
    }


    /**
     * 监视器
     */
    private static NetConfigWatcher watcher = new NetConfigWatcher();


    /**
     * 构造函数
     */
    public Client() {
        IRootConfig conf = Configuration.get("carrie.world.cluster");
        conf.watch(watcher);
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
