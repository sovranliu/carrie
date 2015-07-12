package com.dianping.midasx.base.text;

import com.dianping.midasx.base.character.filter.ByteUTF8Filter;
import com.dianping.midasx.base.interaction.FilterReader;
import com.dianping.midasx.base.io.disk.DiskReadableHandle;
import com.dianping.midasx.base.model.Result;
import com.dianping.midasx.base.text.filter.BytesLineFilter;
import com.dianping.midasx.base.time.Date;
import com.dianping.midasx.base.time.DateTime;
import com.dianping.midasx.base.time.Time;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 文本
 */
public class Text {
    /**
     * 隐藏构造函数
     */
    private Text() { }

    /**
     * 判断字符串是否为空或者空内容
     *
     * @param string 字符串
     * @return 如果输入字符串为空或者空内容则返回true，否则返回false
     */
    public static boolean isBlank(String string) {
        return (null == string) ? true : (string.equals(""));
    }

    /**
     * 重复字符串
     *
     * @param item 字符串片段
     * @param count 重复次数
     * @return 重复后的字符串，错误返回null
     */
    public static<T> String repeat(T item, int count) {
        if(count < 0) {
            throw new IllegalArgumentException();
        }
        else if(0 == count) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < count; i++) {
            builder.append(item);
        }
        return builder.toString();
    }

    /**
     * 重复字符串
     *
     * @param item 字符串片段
     * @param separator 分隔符
     * @param count 重复次数
     * @return 重复后的字符串，错误返回null
     */
    public static<T> String repeat(T item, String separator, int count) {
        if(count < 0) {
            throw new IllegalArgumentException();
        }
        else if(0 == count) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < count; i++) {
            if(i > 0) {
                builder.append(separator);
            }
            builder.append(item);
        }
        return builder.toString();
    }

    /**
     * 判断字符串是否是数字
     *
     * @param string 字符串
     * @return 是否是数字
     */
    public static boolean isNumber(String string) {
        return Pattern.compile("[0-9]*").matcher(string).matches();
    }

    /**
     * 加载文件中的全部内容
     *
     * @param path 文本路径
     * @param encoding 编码格式
     * @return 文本内容
     */
    public static String loadFile(String path, int encoding) throws Exception {
        DiskReadableHandle handle = new DiskReadableHandle();
        if(!handle.open(new File(path))) {
            return null;
        }
        handle.setReadBufferSize(1024);
        FilterReader<byte[], String> reader = new FilterReader<byte[], String>();
        reader.setTarget(handle);
        reader.setReadFilter(new BytesLineFilter(new ByteUTF8Filter()));
        StringBuilder builder = new StringBuilder();
        try {
            while(true) {
                Result<Boolean, String> result = reader.read();
                if(!result.status) {
                    break;
                }
                builder.append(result.info);
            }
        }
        catch(Exception ex) {
            throw ex;
        }
        finally {
            handle.close();
        }
        return builder.toString();
    }

    /**
     * 截取字符串
     *
     * @param text 被截取的字符串
     * @param begin 起始字符，空字符表示从首字符开始
     * @param end 截止字符，空字符表示无截止
     * @return 被截取的字符串
     */
    public static String substring(String text, String begin, String end) {
        int i = 0;
        if(!isBlank(begin)) {
            i = text.indexOf(begin);
            if(-1 == i) {
                return null;
            }
            i += begin.length();
        }
        int j = text.length();
        if(!isBlank(end)) {
            j = text.indexOf(end, i);
            if(-1 == j) {
                return null;
            }
        }
        return text.substring(i, j);
    }

    /**
     * 字符串转对象
     *
     * @param text 文字
     * @return 对象
     */
    public static Object parse(String text) {
        if(null == text) {
            return null;
        }
        String type = substring(text, "(", ")");
        if(null == type) {
            if(isNumber(text)) {
                return Integer.parseInt(text);
            }
            else {
                return text;
            }
        }
        text = text.substring(text.indexOf(")") + 1);
        try {
            if("int".equals(type)) {
                return Integer.parseInt(text);
            }
            else if("long".equals(type)) {
                return Long.parseLong(text);
            }
            else if("byte".equals(type)) {
                return Byte.parseByte(text);
            }
            else if("string".equals(type)) {
                return text;
            }
            else if("date".equals(type)) {
                return Date.parse(text);
            }
            else if("time".equals(type)) {
                return Time.parse(text);
            }
            else if("datetime".equals(type)) {
                return DateTime.parse(text);
            }
            else {
                return text;
            }
        }
        catch(Exception ex) {
            try {
                throw ex;
            }
            catch (Exception e) {
                return null;
            }
        }
    }
}
