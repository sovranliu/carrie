package com.dianping.midasx.utility.net;

import com.dianping.midasx.base.interaction.core.ISession;
import com.dianping.midasx.base.io.net.NetEntry;
import com.dianping.midasx.base.model.core.IHandle;

/**
 * UDP入口
 */
public abstract class UDPEntry extends NetEntry implements IHandle, ISession<NetEntry, Byte[]> {
    /**
     * 打开句柄
     *
     * @return 是否打开成功
     */
    @Override
    public boolean open() {
        return false;
    }

    /**
     * 关闭句柄
     */
    @Override
    public void close() {

    }

    /**
     * 写入
     *
     * @param target 目标
     * @param data   数据
     */
    @Override
    public void write(NetEntry target, Byte[] data) throws Exception {

    }
}
