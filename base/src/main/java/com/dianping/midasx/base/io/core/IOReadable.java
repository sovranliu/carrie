package com.dianping.midasx.base.io.core;

import com.dianping.midasx.base.interaction.core.IReadable;

/**
 * 外设可读接口
 */
public interface IOReadable extends IReadable<byte[]> {
    /**
     * 获取一次读取的数据大小
     *
     * @return 一次读取的数据大小
     */
    public int getReadBufferSize();
}
