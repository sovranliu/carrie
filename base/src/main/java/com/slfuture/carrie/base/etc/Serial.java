package com.slfuture.carrie.base.etc;

import com.slfuture.carrie.base.time.DateTime;

import java.util.Random;

/**
 * 流水工具类
 */
public class Serial {
    /**
     * 滚动流水
     */
    private static long loopLong = 0;
    /**
     * 滚动流水
     */
    private static int loopInteger = 0;


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
     * 生成随机字符串
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String makeRandomString(int length) {
        return makeRandomString("abcdefghijklmnopqrstuvwxyz0123456789", length);
    }

    /**
     * 生成随机字符串
     *
     * @param baseString 基础字符集
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String makeRandomString(String baseString, int length) {
        Random random = new Random();
        StringBuffer builder = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(baseString.length());
            builder.append(baseString.charAt(number));
        }
        return builder.toString();
    }

    /**
     * 构建循环长整型数字
     *
     * @return 循环长整型数字
     */
    public static synchronized long makeLoopLong() {
        if(Long.MAX_VALUE == loopLong) {
            loopLong = 0;
        }
        return ++loopLong;
    }

    /**
     * 构建循环整型数字
     *
     * @return 循环整型数字
     */
    public synchronized static int makeLoopInteger() {
        if(Integer.MAX_VALUE == loopInteger) {
            loopInteger = 0;
        }
        return ++loopInteger;
    }
}
