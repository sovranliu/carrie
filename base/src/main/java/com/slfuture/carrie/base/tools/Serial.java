package com.slfuture.carrie.base.tools;

import com.slfuture.carrie.base.time.DateTime;

/**
 * 流水工具类
 */
public class Serial {
    /**
     * 滚动流水
     */
    private static long index = 0;


    /**
     * 工具类隐藏构造函数
     */
    private Serial() {}

    /**
     * 生成数字流水序号
     *
     * @return 数字流水序号
     */
    public static long makeSerialNumber() {
        return DateTime.now().toLong();
    }

    /**
     * 生成流水字符串
     *
     * @return 流水字符串
     */
    public static String makeSerialString() {
        return String.valueOf(makeSerialNumber());
    }

    /**
     * 构建循环数字
     *
     * @return 循环数字
     */
    public static synchronized long makeLoopNumber() {
        if(index == Long.MAX_VALUE) {
            index = 0;
        }
        index++;
        return index;
    }
}
