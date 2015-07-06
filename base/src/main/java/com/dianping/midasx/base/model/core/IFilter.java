package com.dianping.midasx.base.model.core;

/**
 * 过滤器接口
 *
 * 支持单次将冗余或缺陷的输入对象转换成完整的输出对象
 */
public interface IFilter<O, D> {
    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    public D filter(O origin);
}
