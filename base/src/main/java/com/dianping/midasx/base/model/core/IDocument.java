package com.dianping.midasx.base.model.core;

/**
 * 文档接口
 */
public interface IDocument<C> extends IHandle {
    /**
     * 获取文档内容
     *
     * @return 文档内容
     */
    public C content();

    /**
     * 保存文档
     *
     * @return 执行是否成功
     */
    public boolean save();
}
