package com.slfuture.carrie.base.async;

import com.slfuture.carrie.base.async.core.IOperation;
import com.slfuture.carrie.base.async.core.IOperator;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 操作工类
 */
public class Operator<R> extends Thread implements IOperator<R> {
    /**
     * 操作是否结束
     */
    protected AtomicBoolean isFinished = null;
    /**
     * 当前正在执行的操作
     */
    protected IOperation<R> current = null;


    /**
     * 构造函数
     *
     * @param operation 操作
     */
    public Operator(IOperation<R> operation) {
        super();
        isFinished = new AtomicBoolean(false);
        current = operation;
        start();
    }

    /**
     * 等待操作完成
     */
    public void await() throws InterruptedException {
        synchronized (this) {
            if(isFinished.get()) {
                return;
            }
            this.wait();
        }
    }

    /**
     * 判断操作是否结束
     *
     * @return 操作是否结束
     */
    public boolean isFinished() {
        return this.isFinished.get();
    }

    /**
     * 操作结束回调
     *
     * @param result 操作结果
     * @return 下一道操作，若结束操作则返回null
     */
    @Override
    public IOperation<R> onFinished(R result) {
        return null;
    }

    /**
     * 后台线程执行函数
     */
    @Override
    public void run() {
        while(null != current) {
            current = onFinished(current.onExecute());
        }
        synchronized (this) {
            isFinished.set(true);
            this.notify();
        }
    }
}
