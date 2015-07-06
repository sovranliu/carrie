package com.dianping.midasx.base.async.core;

/**
 * 操作工接口
 */
public interface IOperator<R> {
    /**
     * 操作结束回调
     *
     * @param result 操作结果
     * @return 下一步操作，若结束操作则返回null
     */
    public IOperation<R> onFinished(R result);
}
