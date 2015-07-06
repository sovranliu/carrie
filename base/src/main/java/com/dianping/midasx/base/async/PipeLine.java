package com.dianping.midasx.base.async;

import com.dianping.midasx.base.async.core.IOperation;
import com.dianping.midasx.base.type.core.IArray;
import com.dianping.midasx.base.type.Set;
import com.dianping.midasx.base.type.core.ISet;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 流水线类
 */
public class PipeLine {
    /**
     * 空操作
     */
    private class NullOperation implements IOperation<Void> {
        /**
         * 操作结束回调
         *
         * @return 操作结果
         */
        @Override
        public Void onExecute() {
            return null;
        }
    }

    /**
     * 流水线操作
     */
    private class PipeLineOperator extends Operator<Void> {
        /**
         * 是否需要关闭
         */
        public boolean needClose = false;


        /**
         * 构造函数
         *
         * @param operation 启动操作
         */
        public PipeLineOperator(IOperation<Void> operation) {
            super(operation);
        }

        /**
         * 操作结束回调
         *
         * @param result 操作结果
         * @return 下一道操作，若结束操作则返回null
         */
        @Override
        public IOperation<Void> onFinished(Void result) {
            if(needClose) {
                operatorCount.decrementAndGet();
                synchronized (PipeLine.this) {
                    if(0 == operatorCount.get()) {
                        this.notify();
                    }
                }
                return null;
            }
            while(true) {
                try {
                    IOperation<Void> operation = operationQueue.take();
                    if(null == operation) {
                        continue;
                    }
                    return operation;
                }
                catch (Exception ex) {
                    continue;
                }
            }
        }
    }


    /**
     * 操作集合
     */
    protected ISet<PipeLineOperator> operatorSet = null;
    /**
     * 支持的最大操作数目
     */
    protected int operatorMaxCount = Integer.MAX_VALUE;
    /**
     * 当前的操作数目
     */
    protected AtomicInteger operatorCount = null;
    /**
     * 口令队列
     */
    protected LinkedBlockingQueue<IOperation<Void>> operationQueue = null;
    /**
     * 待处理最大操作个数
     */
    protected int operationMaxCount = Integer.MAX_VALUE;


    /**
     * 获取支持的最大操作工数目
     *
     * @return 支持的最大操作工数目
     */
    public int getOperatorMaxCount() {
        return operatorMaxCount;
    }

    /**
     * 获取待处理操作个数
     *
     * @return 操作个数
     */
    public int getOperationCount() {
        return operationQueue.size();
    }

    /**
     * 获取待处理最大操作个数
     *
     * @return 待处理最大操作个数
     */
    public int getOperationMaxCount() {
        return operationMaxCount;
    }

    /**
     * 启动流水线
     *
     * @param operatorMaxCount 支持的最大操作工数目
     * @param operationMaxCount 待处理最大操作个数
     * @return 执行成功返回true，失败返回false
     */
    public boolean start(int operatorMaxCount, int operationMaxCount) {
        if(operatorMaxCount <= 0 || operationMaxCount <= 0) {
            return false;
        }
        operationQueue = new LinkedBlockingQueue<IOperation<Void>>(operationMaxCount);
        this.operatorMaxCount = operatorMaxCount;
        operatorCount = new AtomicInteger(operatorMaxCount);
        operatorSet = new Set<PipeLineOperator>();
        for(int i = 0; i < operatorMaxCount; i++) {
            operatorSet.add(new PipeLineOperator(new NullOperation()));
        }
        return true;
    }

    /**
     * 停止
     */
    public void stop() throws InterruptedException {
        // 清理线程队列
        for(PipeLineOperator operator : operatorSet) {
            operator.needClose = true;
        }
        operatorSet.clear();
        // 清理任务队列
        operationQueue.clear();
        for(int i = 0; i < operatorMaxCount; i++) {
            operationQueue.put(new NullOperation());
        }
        synchronized (this) {
            this.wait();
        }
    }

    /**
     * 添加一个操作
     *
     * @param operation 操作对象
     */
    public void supply(IOperation<Void> operation) throws InterruptedException {
        operationQueue.put(operation);
    }

    /**
     * 加入操作列表
     *
     * @param operationList 操作列表
     */
    public void supply(IArray<IOperation<Void>> operationList) throws InterruptedException {
        for(IOperation<Void> operation : operationList) {
            supply(operation);
        }
    }
}
