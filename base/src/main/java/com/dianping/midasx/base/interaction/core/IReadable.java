package com.dianping.midasx.base.interaction.core;

import com.dianping.midasx.base.model.Result;

/**
 * 可读接口
 */
public interface IReadable<D> {
    /**
     * 读取
     *
     * @return 结果对象，布尔表示是否未读取到末尾
     */
    public Result<Boolean, D> read() throws Exception;
}
