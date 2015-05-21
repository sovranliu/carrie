package com.slfuture.carrie.base.interaction;

import com.slfuture.carrie.base.interaction.core.IReadable;
import com.slfuture.carrie.base.model.Result;
import com.slfuture.carrie.base.model.core.IFilter;
import com.slfuture.carrie.base.model.core.IReFilter;

/**
 * 带过滤功能的读取器
 */
public class FilterReader<F, T> implements IReadable<T> {
    /**
     * 读取目标
     */
    private IReadable<F> target;
    /**
     * 过滤器
     */
    private IFilter<F, T> filter;


    /**
     * 设置读取目标
     *
     * @param target 可读目标对象
     */
    public void setTarget(IReadable<F> target) {
        this.target = target;
    }

    /**
     * 设置输入过滤器
     *
     * @param filter 输入过滤器
     */
    public void setReadFilter(IFilter<F, T> filter) {
        this.filter = filter;
    }

    /**
     * 读取文件
     *
     * @return 结果对象，布尔表示是否未到文件尾
     */
    @Override
    public Result<Boolean, T> read() throws Exception {
        if(null == filter) {
            return (Result<Boolean, T>) target.read();
        }
        T result;
        if(filter instanceof IReFilter) {
            result = ((IReFilter<F, T>)filter).filter();
            if(null != result) {
                return new Result<Boolean, T>(true, result);
            }
        }
        while(true) {
            Result<Boolean, F> buffer = target.read();
            if(!buffer.status) {
                return new Result<Boolean, T>(false, null);
            }
            result = filter.filter(buffer.info);
            if(null != result) {
                return new Result<Boolean, T>(true, result);
            }
        }
    }
}
