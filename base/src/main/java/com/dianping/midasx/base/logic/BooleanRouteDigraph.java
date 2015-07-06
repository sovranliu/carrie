package com.dianping.midasx.base.logic;

import com.dianping.midasx.base.type.core.ICollection;

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
    public T puts(Boolean key, T parameter) {
        ICollection<T> states = finds(null);
        if(null == super.get(key)) {
            super.put(key, parameter);
        }
        if(key) {
            for(T state : states) {
                if(state.equals(parameter)) {
                    continue;
                }
                if(null == state.get(true)) {
                    state.put(true, parameter);
                }
            }
        }
        else {
            for(T state : states) {
                if(state.equals(parameter)) {
                    continue;
                }
                if(null == state.get(false)) {
                    state.put(false, parameter);
                }
            }
        }
        return null;
    }
}
