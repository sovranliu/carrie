package com.slfuture.carrie.world.rpc.logic;

import com.slfuture.carrie.base.model.core.IReFilter;
import com.slfuture.carrie.base.type.List;
import com.slfuture.carrie.base.type.core.IList;

/**
 * 报文协议
 */
public class Protocol implements IReFilter<byte[], byte[]> {
    /**
     * 报文头字节数
     */
    public final static int PACKET_HEAD_SIZE = 4;

    /**
     * 缓冲链表
     */
    protected IList<byte[]> buffer = new List<byte[]>();
    /**
     * 报文头缓冲
     */
    private byte[] head = new byte[PACKET_HEAD_SIZE];
    /**
     * 起始偏移
     */
    protected int offset = 0;
    /**
     * 报文长度，包括报文头
     */
    protected int length = 0;


    /**
     * 过滤
     *
     * @return 目标数据结构
     */
    @Override
    public byte[] filter() {
        return new byte[0];
    }

    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    @Override
    public byte[] filter(byte[] origin) {
        if(length < PACKET_HEAD_SIZE) {
            if(origin.length < PACKET_HEAD_SIZE - length) {
                System.arraycopy(origin, 0, head, length, origin.length);
                length += origin.length;
                return null;
            }
            else {
                offset = PACKET_HEAD_SIZE - length;
                System.arraycopy(origin, 0, head, length, offset);
                length = PACKET_HEAD_SIZE + ((head[0] & 0xFF) << 24) | ((head[1] & 0xFF)<< 16) | ((head[2] & 0xFF) << 8) | (head[3] & 0xFF);
                if(origin.length == offset) {
                    offset = 0;
                    return null;
                }
            }
        }
        int remain = length;
        for(byte[] bytes : buffer) {
            remain -= bytes.length;
        }
        if(remain + offset <= origin.length) {
            return offer();
        }
        buffer.add(origin);
        return null;
    }

    /**
     * 抽取完整缓冲
     *
     * @return 字节
     */
    private byte[] offer() {
        int size = buffer.size();
        byte[] result = new byte[length];
        int i = 0, j = 0;
        for(byte[] bytes : buffer) {
            if(0 == i) {
                if(i < size - 1) {
                    j += bytes.length - offset;
                    System.arraycopy(bytes, offset, result, 0, j);
                }
                else {
                    System.arraycopy(bytes, offset, result, 0, length);
                    if(offset + length < bytes.length) {
                        offset += length;
                        buffer.clear();
                        buffer.add(bytes);
                        length = 0;
                    }
                    break;
                }
            }
            else {
                if(i < size - 1) {
                    System.arraycopy(bytes, 0, result, j, bytes.length);
                    j += bytes.length - PACKET_HEAD_SIZE;
                }
                else {
                    System.arraycopy(bytes, 0, result, j, length - j);
                    if(length - j < bytes.length) {
                        offset = length - j;
                        buffer.clear();
                        buffer.add(bytes);
                        length = 0;
                    }
                    break;
                }
            }
            i++;
        }
        return result;
    }

    /**
     * 转化为报文字节
     *
     * @param bytes 待传输的内容
     * @return 报文字节
     */
    public static byte[] convert(byte[] bytes) {
        byte[] result = new byte[bytes.length + PACKET_HEAD_SIZE];
        result[0] = (byte) ((bytes.length >> 24) & 0xFF);
        result[1] = (byte) ((bytes.length >> 16) & 0xFF);
        result[2] = (byte) ((bytes.length >> 8) & 0xFF);
        result[3] = (byte) (bytes.length & 0xFF);
        System.arraycopy(bytes, 0, result, 4, bytes.length);
        return result;
    }
}
