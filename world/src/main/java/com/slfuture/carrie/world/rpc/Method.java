package com.slfuture.carrie.world.rpc;

import java.io.Serializable;

/**
 * 指令的方法字段
 */
public class Method implements Serializable {
    /**
     * 方法名称
     */
    public String name;
    /**
     * 参数类型
     */
    public Class<?>[] parameterTypes;
}
