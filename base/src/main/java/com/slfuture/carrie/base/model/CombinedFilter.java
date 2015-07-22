package com.slfuture.carrie.base.model;

import com.slfuture.carrie.base.model.core.IReFilter;
import com.slfuture.carrie.base.model.core.IFilter;
import com.slfuture.carrie.base.type.List;

/**
 * 组合式过滤器
 */
public class CombinedFilter<O, D> extends List<IFilter<?, ?>> implements IReFilter<O, D> {
    /**
     * 过滤
     *
     * @return 目标数据结构
     */
    @Override
    public D filter() {
        return filter(null);
    }

    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    @Override
    public D filter(O origin) {
        int j = -1; // 最后一个过滤干净的过滤器的索引
        Object result = origin;
        for(int i = 0; i < this.size(); i++) {
            IFilter filter = this.get(i);
            if(null == result) {
                if(filter instanceof IReFilter) {
                    result = ((IReFilter)filter).filter();
                    if(null == result) {
                        j = i;
                    }
                }
                continue;
            }
            result = filter.filter(result);
            if(null == result) {
                if(i - 1 > j) {
                    i = j;
                }
            }
        }
        return (D) result;
    }
}
