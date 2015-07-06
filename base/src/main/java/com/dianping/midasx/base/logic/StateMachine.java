package com.dianping.midasx.base.logic;

import com.dianping.midasx.base.logic.core.IStateMachine;

/**
 * 状态机类
 */
public class StateMachine<I, S> implements IStateMachine<I, S> {
    /**
     * 当前状态
     */
    protected State<I, S> current = null;


    /**
     * 构造函数
     *
     * @param start 初始状态
     */
    public StateMachine(State<I, S> start) {
        this.current = start;
    }

    /**
     * 当前状态
     *
     * @return 状态
     */
    @Override
    public S status() {
        return current.get();
    }

    /**
     * 写入
     *
     * @param data 数据
     */
    @Override
    public void write(I data) throws Exception {
        State<I, S> result = current.get(data);
        if(null == result) {
            throw new IllegalStateException();
        }
        current = result;
    }
}
