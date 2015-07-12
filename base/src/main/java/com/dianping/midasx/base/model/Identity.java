package com.dianping.midasx.base.model;

import java.io.Serializable;

/**
 * 标志类
 */
public class Identity<T> implements Serializable {
    /**
     * 标志值
     */
    public T value;


    /**
     * 构造函数
     */
    public Identity() { }

    /**
     * 构造函数
     *
     * @param value 标志值
     */
    public Identity(T value) {
        this.value = value;
    }
}
