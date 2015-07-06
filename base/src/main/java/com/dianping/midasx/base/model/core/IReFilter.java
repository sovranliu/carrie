package com.dianping.midasx.base.model.core;

/**
 * 多重过滤器接口
 *
 * 支持多次将冗余或缺陷的输入对象转换成完整的输出对象
 */
public interface IReFilter<O, D> extends IFilter<O, D> {
    /**
     * 过滤
     *
     * @return 目标数据结构
     */
    public D filter();
}
