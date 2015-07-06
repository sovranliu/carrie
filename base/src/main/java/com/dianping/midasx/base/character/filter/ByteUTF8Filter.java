package com.dianping.midasx.base.character.filter;

import com.dianping.midasx.base.character.filter.core.IByteCharacterFilter;

/**
 * 字节转UTF-8过滤器
 */
public class ByteUTF8Filter implements IByteCharacterFilter {
    /**
     * 缓冲，最大支持3个字节的UTF码
     */
    protected byte[] buffer = new byte[3];


    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    @Override
    public Character filter(Byte origin) {
        if(buffer[0] >= (byte) 0xE0 && buffer[0] < (byte) 0xF0) {
            if(0 == buffer[1]) {
                buffer[1] = origin;
                return null;
            }
            else if(0 == buffer[2]) {
                buffer[2] = origin;
                // return (char) ((buffer[0] << 12) | ((buffer[1] & 0x3F) << 6) | (buffer[2] & 0x3F));
                try {
                    String result = new String(buffer, "UTF-8");
                    buffer[0] = 0;
                    buffer[1] = 0;
                    buffer[2] = 0;
                    return result.toCharArray()[0];
                }
                catch(Exception ex) {
                    throw new IllegalAccessError();
                }
            }
        }
        else if(buffer[0] >= (byte) 0xC0 && buffer[0] < (byte) 0xE0) {
            byte[] bytes = new byte[2];
            bytes[0] = buffer[0];
            bytes[1] = origin;
            // return (char) ((buffer[0] << 11) | (buffer[1] & 0x3F));
            try {
                String result = new String(bytes, "UTF-8");
                buffer[0] = 0;
                buffer[1] = 0;
                buffer[2] = 0;
                return result.toCharArray()[0];
            }
            catch(Exception ex) {
                throw new IllegalAccessError();
            }
        }
        else if(origin > 0) {
            byte[] bytes = new byte[1];
            bytes[0] = origin;
            // char result = (char)origin.byteValue();
            try {
                String result = new String(bytes, "UTF-8");
                buffer[0] = 0;
                buffer[1] = 0;
                buffer[2] = 0;
                return result.toCharArray()[0];
            }
            catch(Exception ex) {
                throw new IllegalAccessError();
            }
        }
        else {
            buffer[0] = origin;
            buffer[1] = 0;
            buffer[2] = 0;
            return null;
        }
        throw new IndexOutOfBoundsException();
    }
}
