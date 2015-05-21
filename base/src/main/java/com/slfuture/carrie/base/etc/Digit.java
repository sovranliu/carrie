package com.slfuture.carrie.base.etc;

/**
 * 数字相关类
 */
public class Digit {
    /**
     * 当前流水号
     */
    private static int serialNumber = 0;


    /**
     * 生成流水号
     *
     * @return 流水号
     */
    public synchronized static int makeSerialNumber() {
        return ++serialNumber;
    }
}
