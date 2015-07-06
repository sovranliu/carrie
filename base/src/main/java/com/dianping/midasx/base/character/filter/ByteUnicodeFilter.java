package com.dianping.midasx.base.character.filter;

import com.dianping.midasx.base.character.filter.core.IByteCharacterFilter;

/**
 * 字节转Unicode过滤器
 */
public class ByteUnicodeFilter implements IByteCharacterFilter {
    /**
     * 缓冲
     */
    protected byte buffer = 0;
    /**
     * 缓冲是否被使用
     */
    protected boolean isBuffered = false;


    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    @Override
    public Character filter(Byte origin) {
        if(!isBuffered) {
            buffer = origin;
            isBuffered = true;
            return null;
        }
        Character result = (char) (((buffer & 0xFF) << 8) | (origin & 0xFF));
        isBuffered = false;
        return result;
    }
}
