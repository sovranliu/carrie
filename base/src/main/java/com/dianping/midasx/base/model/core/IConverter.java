package com.dianping.midasx.base.model.core;

/**
 * 转换器接口
 */
public interface IConverter<O, D> {
    /**
     * 转换
     *
     * @param origin 原对象
     * @return 目标对象
     */
    public D convert(O origin);

    /**
     * 逆转
     *
     * @param destination 目标对象
     * @return 原对象
     */
    public O invert(D destination);
}
