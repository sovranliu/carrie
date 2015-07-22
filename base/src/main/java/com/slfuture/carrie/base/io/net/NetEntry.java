package com.slfuture.carrie.base.io.net;

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
     * 比较
     *
     * @param object 比较对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object object) {
        if (ip.equals(((NetEntry)object).ip) && port == ((NetEntry)object).port) {
            return true;
        }
        return false;
    }

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
