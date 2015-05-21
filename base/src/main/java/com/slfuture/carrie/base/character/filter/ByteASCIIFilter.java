package com.slfuture.carrie.base.character.filter;

import com.slfuture.carrie.base.character.filter.core.IByteCharacterFilter;

/**
 * 字节转ASCII过滤器
 */
public class ByteASCIIFilter implements IByteCharacterFilter {
    /**
     * 缓冲
     */
    protected byte buffer = 0;


    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    @Override
    public Character filter(Byte origin) {
        if(0 == buffer) {
            if(origin < 0) {
                buffer = origin;
                return null;
            }
            else {
                return (char)origin.byteValue();
            }
        }
        Character result = (char) (((buffer & 0xFF) << 8) | (origin & 0xFF));
        buffer = 0;
        return result;
    }
}
