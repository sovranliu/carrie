package com.slfuture.carrie.utility.rpc;

import com.slfuture.carrie.base.logic.core.ICondition;
import com.slfuture.carrie.base.type.core.IList;

/**
 * RPC 协议类
 *
 * 方法调用：size.token.class.id.method.parameters
 * 调用结果：size.token.result
 */
public class Protocol {
    /**
     * 口令
     */
    public int token;
    /**
     * 簇名
     */
    public String cluster;
    /**
     * 条件
     */
    public ICondition<?> condition;
    /**
     * 方法名称
     */
    public String method;
    /**
     * 参数列表
     */
    public IList<Object> parameters;
}
