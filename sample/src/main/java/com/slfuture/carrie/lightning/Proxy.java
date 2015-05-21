package com.slfuture.carrie.lightning;

/**
 * 代理类
 */
public class Proxy {
    /**
     * 按照指定条件搜索对象
     *
     * @param condition 条件语句，簇名，簇名+逗号+条件
     * @return 结果对象或结果对象集合
     */
    public static Object $$(String condition) {
        return null;
    }

    /**
     * 获取指定外联的值
     *
     * @param link 外联字符串
     * @return 值对象，可以是值类型，也可以是代理对象
     */
    public Object $(String link) {
        return null;
    }

    /**
     * 调用指定方法
     *
     * @param method 方法名称
     * @param parameters 参数列表
     * @return 返回值
     */
    public Object call(String method, Object...parameters) {
        return null;
    }
}
