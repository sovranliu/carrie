package com.slfuture.carrie.lightning.action.core;

import com.slfuture.carrie.lightning.context.PageContext;
import com.slfuture.carrie.lightning.context.PageVisitor;

/**
 * 动作处理器接口
 */
public interface IAction {
    /**
     * 调用
     *
     * @param visitor 访问者
     * @param context 上下文
     * @return 下一步动作
     */
    public String execute(PageVisitor visitor, PageContext context) throws Exception;
}
