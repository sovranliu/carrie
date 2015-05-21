package com.slfuture.carrie.base.model.core;

/**
 * 有值接口
 */
public interface IValuable<T> {
    /**
     * 获取
     *
     * @return 值
     */
    public T get();

    /**
     * 设置
     *
     * @param value 值
     */
    public void set(T value);
}
