package com.slfuture.carrie.base.logic.core;

import com.slfuture.carrie.base.interaction.core.IWritable;
import com.slfuture.carrie.base.model.core.IStatus;

/**
 * 状态机接口
 */
public interface IStateMachine<I, S> extends IWritable<I>, IStatus<S> { }
