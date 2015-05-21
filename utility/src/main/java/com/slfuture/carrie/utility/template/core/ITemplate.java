package com.slfuture.carrie.utility.template.core;

import com.slfuture.carrie.utility.template.Context;

/**
 * 模板接口
 */
public interface ITemplate {
    /**
     * 获取模板内容
     *
     * @return 模板内容
     */
    public String content();

    /**
     * 设置模板内容
     *
     * @param content 模板内容
     */
    public void setContent(String content);

    /**
     * 渲染
     *
     * @param context 上下文
     * @return 渲染后的文本
     */
    public String render(Context context) throws Exception;
}
