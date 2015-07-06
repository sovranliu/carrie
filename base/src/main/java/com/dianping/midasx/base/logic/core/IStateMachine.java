package com.dianping.midasx.base.logic.core;

import com.dianping.midasx.base.interaction.core.IWritable;
import com.dianping.midasx.base.model.core.IStatus;

/**
 * 状态机接口
 */
public interface IStateMachine<I, S> extends IWritable<I>, IStatus<S> { }
