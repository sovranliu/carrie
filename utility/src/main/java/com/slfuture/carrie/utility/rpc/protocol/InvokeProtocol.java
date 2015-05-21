package com.slfuture.carrie.utility.rpc.protocol;

import com.slfuture.carrie.base.model.core.IReFilter;

/**
 * 远程调用协议
 *
 * 格式：token.name.condition.method.parameters
 */
public class InvokeProtocol implements IReFilter<byte[], byte[]> {

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
        return new byte[0];
    }
}
