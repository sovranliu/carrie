package com.slfuture.carrie.base.async.core;

/**
 * 操作接口
 */
public interface IOperation<R> {
    /**
     * 操作结束回调
     *
     * @return 操作结果
     */
    public R onExecute();
}
