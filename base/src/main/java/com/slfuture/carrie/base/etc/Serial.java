package com.slfuture.carrie.base.etc;

import com.slfuture.carrie.base.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.util.Random;

/**
 * 流水工具类
 */
public class Serial {
    /**
     * 默认的密码字符串组合
     * apache校验下载的文件采用该组合
     */
    private static final char HEXDIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * 加解密类实例
     */
    private static MessageDigest messagedigest = null;
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

    /**
     * 获取字符串的MD5码
     *
     * @param string 待操作的字符串
     * @return 字符串MD5码
     */
    public static String getMD5String(String string) {
        return getMD5String(string.getBytes());
    }

    /**
     * 获取字符串的哈希散列值
     *
     * @param string 待操作的字符串
     * @return 哈希散列值
     */
    public static String getSHA1(String string) {
        return SHA1.hex_sha1(string);
    }

    /**
     * 获取字节数组的MD5码
     *
     * @param bytes 待操作的字节数组
     * @return 字节数组MD5码
     */
    public static String getMD5String(byte[] bytes) {
        if(null == getMessageDigest()) {
            return null;
        }
        synchronized(Serial.class) {
            messagedigest.update(bytes);
            return arrayToHex(messagedigest.digest());
        }
    }

    /**
     * 获取文件MD5码
     *
     * @param file 被操作文件
     * @return 文件MD5码，失败返回null
     */
    public static String getFileMD5String(File file) {
        if(null == file || !file.exists()) {
            return null;
        }
        if(null == getMessageDigest()) {
            return null;
        }
        FileInputStream in = null;
        FileChannel ch = null;
        String result = null;
        try
        {
            in = new FileInputStream(file);
            ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            synchronized(Serial.class) {
                messagedigest.update(byteBuffer);
                result = arrayToHex(messagedigest.digest());
            }
            closeMappedBuffer(byteBuffer);
        }
        catch(Exception ex) {
            return null;
        }
        finally {
            if(null != ch) {
                try {
                    ch.close();
                }
                catch (IOException e) { }
                ch = null;
            }
            if(null != in) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
        }
        return result;
    }

    /**
     * 获取可用加解密类实例
     *
     * @return 可用加解密类实例
     */
    protected static MessageDigest getMessageDigest() {
        if(null != messagedigest) {
            return messagedigest;
        }
        synchronized(Serial.class) {
            if(null != messagedigest) {
                return messagedigest;
            }
            try {
                messagedigest = MessageDigest.getInstance("MD5");
            }
            catch (NoSuchAlgorithmException ex){
                messagedigest = null;
            }
        }
        return messagedigest;
    }

    /**
     * 获取字节数组转字符串
     *
     * @param bytes 待操作的字节数组
     * @return 字符串
     */
    private static String arrayToHex(byte bytes[]) {
        return arrayToHex(bytes, 0, bytes.length);
    }

    /**
     * 字节数组转字符串
     *
     * @param bytes 待操作的字节数组
     * @param begin 其实索引
     * @param length 字节长度
     * @return 字符串
     */
    private static String arrayToHex(byte bytes[], int begin, int length) {
        StringBuffer stringbuffer = new StringBuffer(2 * length);
        int k = begin + length;
        for (int l = begin; l < k; l++) {
            stringbuffer.append(HEXDIGITS[(bytes[l] & 0xf0) >> 4]);
            stringbuffer.append(HEXDIGITS[bytes[l] & 0xf]);
        }
        return stringbuffer.toString();
    }

    /**
     * 回收缓存
     *
     * @param buffer 缓存对象
     */
    public static final void closeMappedBuffer(final Buffer buffer) {
        if (null == buffer)
            return;
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    final Method cleanerMethod = buffer.getClass().getMethod("cleaner");
                    if (null == cleanerMethod)
                        return null;
                    cleanerMethod.setAccessible(true);
                    final Object cleanerObj = cleanerMethod.invoke(buffer);
                    if (null == cleanerObj)
                        return null;
                    final Method cleanMethod = cleanerObj.getClass().getMethod("clean");
                    if (null == cleanMethod)
                        return null;
                    cleanMethod.invoke(cleanerObj);
                }
                catch (final Throwable e) { }
                return null;
            }
        });
    }
}
