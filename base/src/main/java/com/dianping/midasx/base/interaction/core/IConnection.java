package com.dianping.midasx.base.interaction.core;

import com.dianping.midasx.base.model.core.IPointer;
import com.dianping.midasx.base.model.core.IStatus;

/**
 * 连接接口
 */
public interface IConnection<T, D> extends IWritable<D>, IListenable<D>, IStatus<Integer>, IPointer<T> {
    /**
     * 连接
     *
     * @param target 目标
     * @return 连接是否成功
     */
    public boolean connect(T target) throws Exception;

    /**
     * 断开
     */
    public void disconnect();

    /**
     * 状态变化回调
     *
     * @param status 新状态
     */
    public void onStatusChanged(int status);
}
