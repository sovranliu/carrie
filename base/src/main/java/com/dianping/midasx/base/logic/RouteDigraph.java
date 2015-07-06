package com.dianping.midasx.base.logic;

import com.dianping.midasx.base.logic.core.IState;
import com.dianping.midasx.base.type.MappingDigraph;

/**
 * 路由图类
 *
 * I 输入，C 上下文, T 子类
 */
public abstract class RouteDigraph<I, C, T extends RouteDigraph<I, C, T>> extends MappingDigraph<I, T> implements IState<I, T> {
    /**
     * 路由选择
     *
     * @param value 值
     * @return 输入
     */
    public abstract I onRoute(C value);

    /**
     * 路由
     *
     * @param value 值
     * @return 最终状态
     */
    public I route(C value) {
        RouteDigraph<I, C, T> state = this;
        while(true) {
            I result = state.onRoute(value);
            if(null == result) {
                return null;
            }
            RouteDigraph<I, C, T> next = state.get(result);
            if(null == next) {
                return result;
            }
            state = next;
        }
    }
}
