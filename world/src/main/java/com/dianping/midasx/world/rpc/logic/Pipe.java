package com.dianping.midasx.world.rpc.logic;

import com.dianping.midasx.base.model.GlobeIdentity;
import com.dianping.midasx.base.type.core.ISet;
import com.dianping.midasx.base.type.safe.Set;
import com.dianping.midasx.utility.net.TCPConnection;
import com.dianping.midasx.world.rpc.Method;

/**
 * 同类应用机器的管道类
 */
public abstract class Pipe {
    /**
     * 客户端TCP连接类
     */
    public class ClientTCPConnection extends TCPConnection {
        /**
         * 状态变化回调
         *
         * @param status 新状态
         */
        @Override
        public void onStatusChanged(int status) {
            if(TCPConnection.STATUS_DISCONNECTED == status) {
                connections.remove(ClientTCPConnection.this);
            }
            else if(TCPConnection.STATUS_CONNECTED == status) {
                connections.add(ClientTCPConnection.this);
            }
            logger.info(ClientTCPConnection.this.toString() + " STATUS = " + status);
        }

        /**
         * 监听回调
         *
         * @param data 内容
         */
        @Override
        public void onRead(byte[] data) {

        }
    }


    /**
     * 连接池
     */
    public ISet<ClientTCPConnection> connections = new Set<ClientTCPConnection>();


    /**
     * 调用本应用的方法
     *
     * @param id 被调用对象标志
     * @param method 调用方法名称
     * @param parameters 调用参数
     * @return 调用返回值
     */
    public Object call(GlobeIdentity<Object> id, Method method, Object...parameters) {
        return null;
    }
}
