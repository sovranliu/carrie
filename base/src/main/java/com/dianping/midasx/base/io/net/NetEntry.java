package com.dianping.midasx.base.io.net;

import java.io.Serializable;

/**
 * 网络入口
 */
public class NetEntry implements Cloneable, Serializable {
    /**
     * 地址
     */
    public String ip;
    /**
     * 端口
     */
    public int port;


    /**
     * 转化为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return ip + ":" + port;
    }
}
