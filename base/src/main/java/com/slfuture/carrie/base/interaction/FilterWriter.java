package com.slfuture.carrie.base.interaction;

import com.slfuture.carrie.base.interaction.core.IWritable;
import com.slfuture.carrie.base.model.core.IFilter;
import com.slfuture.carrie.base.model.core.IReFilter;

/**
 * 带过滤功能的写入器
 */
public class FilterWriter<F, T> implements IWritable<F> {
    /**
     * 读取目标
     */
    private IWritable<T> target;
    /**
     * 过滤器
     */
    private IFilter<F, T> filter;


    /**
     * 设置读取目标
     *
     * @param target 可读目标对象
     */
    public void setTarget(IWritable<T> target) {
        this.target = target;
    }

    /**
     * 设置输出过滤器
     *
     * @param filter 输出过滤器
     */
    public void setWriteFilter(IFilter<F, T> filter) {
        this.filter = filter;
    }

    /**
     * 输出数据
     *
     * @param data 数据对象
     */
    @Override
    public void write(F data) throws Exception {
        if(null == filter) {
            target.write((T)data);
            return;
        }
        T result = filter.filter(data);
        if(null == result) {
            return;
        }
        target.write(result);
        if(filter instanceof IReFilter) {
            while(true) {
                result = ((IReFilter<F, T>) filter).filter();
                if(null == result) {
                    break;
                }
                target.write(result);
            }
        }
    }
}
