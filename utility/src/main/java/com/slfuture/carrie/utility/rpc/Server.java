package com.slfuture.carrie.utility.rpc;

import com.slfuture.carrie.utility.net.TCPConnection;
import com.slfuture.carrie.utility.net.TCPEntry;
import com.slfuture.carrie.utility.rpc.core.IEntityManager;

/**
 * 远程调用服务方
 */
public class Server extends TCPEntry {
    /**
     * 注册/替代实体管理器
     *
     * @param name 注册名
     * @param entityManager 实体管理器,若为null表示解除注册名
     */
    public void register(String name, IEntityManager<?> entityManager) {

    }

    /**
     * 目标状态变化
     *
     * @param target 目标
     * @param status 新状态
     */
    @Override
    public void onStatusChanged(TCPConnection target, int status) {
        logger.info(target.toString() + " STATUS = " + status);
    }

    /**
     * 监听回调
     *
     * @param target 目标
     * @param data   内容
     */
    @Override
    public void onRead(TCPConnection target, byte[] data) {

    }
}
