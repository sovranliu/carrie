package com.slfuture.carrie.base.logic.core;

import com.slfuture.carrie.base.type.core.ISignedDigraph;

/**
 * 状态接口
 * I 输入类型，S 派生类型
 */
public interface IState<I, S extends IState<I, S>> extends ISignedDigraph<I, S> { }
