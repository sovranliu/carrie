package com.dianping.midasx.base.interaction.core;

import java.io.IOException;

/**
 * 可写接口
 */
public interface IWritable<D> {
    /**
     * 写入
     *
     * @param data 数据
     */
    public void write(D data) throws Exception;
}
