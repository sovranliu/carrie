package com.slfuture.carrie.world.logic;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 调用结果
 */
public class Result {
    /**
     * 日志对象
     */
    protected static Logger logger = Logger.getLogger(Result.class);
    /**
     * 被调用簇名称
     */
    public String clusterName;
    /**
     * 调用者报文句柄
     */
    public int tag;
    /**
     * 调用结果
     */
    public Object result;


    /**
     * 构造函数
     */
    public Result() { }

    /**
     * 构造函数
     *
     * @param clusterName 簇名称
     * @param tag 调用者报文句柄
     * @param result 调用结果
     */
    public Result(String clusterName, int tag, Object result) {
        this.clusterName = clusterName;
        this.tag = tag;
        this.result = result;
    }

    /**
     * 从字节流中解析出调用结果对象
     * tag|cluster|result
     *
     * @param bytes 字节数组
     * @return 调用结果对象
     */
    public static Result build(byte[] bytes) {
        Result result = null;
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            result = new Result();
            result.tag = stream.readInt();
            result.clusterName = (String) stream.readObject();
            result.result = stream.readObject();
        }
        catch(Exception ex) {
            logger.error("Result.build(?) execute failed", ex);
            return null;
        }
        finally {
            if(null != stream) {
                try {
                    stream.close();
                }
                catch(Exception ex) {
                    logger.error("call stream.close() failed", ex);
                }
            }
        }
        return result;
    }

    /**
     * 将调用结果对象转化为字节数组
     * tag|cluster|result
     *
     * @return 字节数组
     */
    public byte[] toBytes() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = null;
        try {
            objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeInt(tag);
            objectStream.writeObject(clusterName);
            objectStream.writeObject(result);
        }
        catch(Exception ex) {
            logger.error("Result.toBytes() execute failed", ex);
            return null;
        }
        finally {
            if(null != objectStream) {
                try {
                    objectStream.close();
                }
                catch(Exception ex) {
                    logger.error("call stream.close() failed", ex);
                }
            }
        }
        return byteStream.toByteArray();
    }
}
