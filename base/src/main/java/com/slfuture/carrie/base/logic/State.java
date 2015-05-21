package com.slfuture.carrie.base.logic;

import com.slfuture.carrie.base.logic.core.IState;
import com.slfuture.carrie.base.model.core.IValuable;
import com.slfuture.carrie.base.type.Map;
import com.slfuture.carrie.base.type.SignedDigraph;
import com.slfuture.carrie.base.type.core.ILink;
import com.slfuture.carrie.base.type.core.IMap;

/**
 * 状态类
 */
public class State<I, V> extends SignedDigraph<I, State<I, V>> implements IState<I, State<I, V>>, IValuable<V> {
    /**
     * 值
     */
    private V value;


    /**
     * 构造函数
     */
    public State() { }

    /**
     * 构造函数
     *
     * @param value 值
     */
    public State(V value) {
        this.value = value;
    }

    /**
     * 构建状态图
     *
     * @param values 状态序列
     * @param inputs 输入序列
     */
    public static <I, V> State<I, V> build(V[] values, I[] inputs) {
        IMap<V, State<I, V>> map = new Map<V, State<I, V>>();
        State<I, V> result = null;
        for(int i = 0; i < inputs.length; i++) {
            State<I, V> origin = map.get(values[2 * i]);
            if(null == origin) {
                origin = new State<I, V>(values[2 * i]);
                map.put(values[2 * i], origin);
            }
            if(0 == i) {
                result = origin;
            }
            State<I, V> destination = map.get(values[2 * i + 1]);
            if(null == destination) {
                destination = new State<I, V>(values[2 * i + 1]);
                map.put(values[2 * i + 1], destination);
            }
            origin.put(inputs[i], destination);
        }
        return result;
    }

    /**
     * 获取
     *
     * @return 值
     */
    @Override
    public V get() {
        return value;
    }

    /**
     * 设置
     *
     * @param value 值
     */
    @Override
    public void set(V value) {
        this.value = value;
    }

    /**
     * 深度克隆
     *
     * @return 克隆对象
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        State<I, V> result = (State<I, V>) super.clone();
        for(ILink<I, State<I, V>> link : this) {
            result.put(link.getOrigin(), (State<I, V>) (link.getDestination().clone()));
        }
        return result;
    }
}
