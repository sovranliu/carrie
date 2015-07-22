package com.slfuture.carrie.base.text.filter;

import com.slfuture.carrie.base.character.filter.core.IByteCharacterFilter;
import com.slfuture.carrie.base.text.filter.core.IBytesStringFilter;

/**
 * 字节串转字符串过滤器
 */
public class BytesStringFilter implements IBytesStringFilter {
    /**
     * 字节转字符过滤器
     */
    protected IByteCharacterFilter filter = null;


    /**
     * 构造函数
     *
     * @param filter 字节转字符过滤器
     */
    public BytesStringFilter(IByteCharacterFilter filter) {
        this.filter = filter;
    }

    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    @Override
    public String filter(byte[] origin) {
        StringBuilder builder = new StringBuilder();
        for(byte b : origin) {
            Character c = filter.filter(b);
            if(null == c) {
                continue;
            }
            builder.append(c);
        }
        return builder.toString();
    }
}
