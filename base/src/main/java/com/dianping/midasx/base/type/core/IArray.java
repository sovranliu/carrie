package com.dianping.midasx.base.type.core;

/**
 * 数组接口
 */
public interface IArray<I> extends ISequence<I> {
    /**
     * 设置指定位置的元素
     *
     * @param index 指定位置;
     * @param item 待设置的元素对象
     * @return 原值
     */
    public I set(int index, I item);
}
