package com.slfuture.carrie.base.time;

import java.io.Serializable;

/**
 * 时间段类
 */
public class Period implements Serializable {
    /**
     * 开始时间
     */
    public DateTime begin;
    /**
     * 结束时间
     */
    public DateTime end;



    /**
     * 构造函数
     */
    public Period() { }

    /**
     * 构造函数
     *
     * @param begin 开始时间
     * @param end 结束时间
     */
    public Period(DateTime begin, DateTime end) {
        this.begin = begin;
        this.end = end;
    }

    /**
     * 时间段中包含几天
     *
     * @return 左闭右开区间差值
     */
    public int days() {
        return end.getDate().toInteger() - begin.getDate().toInteger();
    }
}
