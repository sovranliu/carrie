package com.dianping.midasx.base.text.filter;

import com.dianping.midasx.base.character.filter.core.IByteCharacterFilter;
import com.dianping.midasx.base.model.core.IReFilter;

/**
 * 字节转字符串过滤器
 */
public class BytesLineFilter extends BytesStringFilter implements IReFilter<byte[], String> {
    /**
     * 字符串缓冲
     */
    protected StringBuilder buffer = null;


    /**
     * 构造函数
     *
     * @param filter 字节转字符过滤器
     */
    public BytesLineFilter(IByteCharacterFilter filter) {
        super(filter);
        buffer = new StringBuilder();
    }

    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    @Override
    public String filter(byte[] origin) {
        String result = super.filter(origin);
        if(null == result) {
            return filter();
        }
        buffer.append(result);
        return filter();
    }

    /**
     * 过滤
     *
     * @return 目标数据结构
     */
    @Override
    public String filter() {
        int i = buffer.indexOf("\n");
        if(-1 == i) {
            return null;
        }
        String result = buffer.substring(0, i);
        buffer.delete(0, i + 1);
        return result;
    }
}
