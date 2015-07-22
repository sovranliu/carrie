package com.slfuture.carrie.base.io.core;

import com.slfuture.carrie.base.interaction.core.IReadable;

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
