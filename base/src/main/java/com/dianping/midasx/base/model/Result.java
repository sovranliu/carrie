package com.dianping.midasx.base.model;

/**
 * 结果类
 */
public class Result<S, I> {
    /**
     * 结果状态
     */
    public S status;
    /**
     * 结果包含的信息
     */
    public I info;


    /**
     * 构造函数
     */
    public Result() { }

    /**
     * 构造函数
     *
     * @param status 状态
     * @param info 信息
     */
    public Result(S status, I info) {
        this.status = status;
        this.info = info;
    }
}
