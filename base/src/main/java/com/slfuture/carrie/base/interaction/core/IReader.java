package com.slfuture.carrie.base.interaction.core;

import com.slfuture.carrie.base.model.Result;

/**
 * 读者接口
 */
public interface IReader<T, D> {
    /**
     * 读取
     *
     * @param target 目标
     * @return 数据
     */
    public Result<Boolean, D> read(T target);
}
