package com.dianping.midasx.base.interaction.core;

/**
 * 写者接口
 */
public interface IWriter<T, D> {
    /**
     * 写入
     *
     * @param target 目标
     * @param data 数据
     */
    public void write(T target, D data) throws Exception;
}
