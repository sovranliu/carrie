package com.slfuture.carrie.base.interaction.core;

import com.slfuture.carrie.base.model.Result;

/**
 * 回调接口
 */
public interface ICallback<S, T> {
    /**
     * 结果回调
     *
     * @param result 结果对象
     */
    public void onResult(Result<S, T> result);
}
