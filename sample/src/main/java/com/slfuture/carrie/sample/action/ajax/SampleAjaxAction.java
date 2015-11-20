package com.slfuture.carrie.sample.action.ajax;

import com.slfuture.carrie.lightning.action.core.IAction;
import com.slfuture.carrie.lightning.context.PageContext;
import com.slfuture.carrie.lightning.context.PageVisitor;

/**
 * 范例ajax动作
 */
public class SampleAjaxAction implements IAction {
    /**
     * 调用
     *
     * @param visitor 访问者
     * @param context 上下文
     * @return 下一步动作
     */
    @Override
    public String execute(PageVisitor visitor, PageContext context) throws Exception {
        context.set("id", "9527");
        return "sample.html";
    }
}
