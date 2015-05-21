package com.slfuture.carrie.base.logic;

import com.slfuture.carrie.base.type.core.ISet;

/**
 * 布尔路由图类
 *
 * P 上下文, T 子类
 */
public abstract class BooleanRouteDigraph<P, T extends BooleanRouteDigraph<P, T>> extends RouteDigraph<Boolean, P, T> {
    /**
     * 设置状态转换
     *
     * @param key 输入信号
     * @param parameter 状态
     * @return 新状态码
     */
    public T putAll(Boolean key, T parameter) {
        ISet<T> states = find(null);
        if(null == super.get(key)) {
            super.put(key, parameter);
        }
        states.remove(parameter);
        if(key) {
            for(T state : states) {
                if(null == state.get(true)) {
                    state.put(true, parameter);
                }
            }
        }
        else {
            for(T state : states) {
                if(null == state.get(false)) {
                    state.put(false, parameter);
                }
            }
        }
        return null;
    }
}
