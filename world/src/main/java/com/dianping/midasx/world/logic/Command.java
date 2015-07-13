package com.dianping.midasx.world.logic;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

/**
 * 调用命令
 */
public class Command {
    /**
     * 调用命令方法
     */
    public static class CommandMethod {
        /**
         * 方法名称
         */
        public String method;
        /**
         * 方法参数列表
         */
        public Object[] parameters;
    }


    /**
     * 日志对象
     */
    protected static Logger logger = Logger.getLogger(Command.class);
    /**
     * 调用者标志符
     */
    public int callerId;
    /**
     * 被调用簇名称
     */
    public String cluster;
    /**
     * 被调用对象标志符，null表示调用簇方法
     */
    public Object targetId = null;
    /**
     * 调用方法
     */
    public CommandMethod method = null;
    /**
     * 附属信息：调用者报文句柄
     */
    public int tag = 0;


    /**
     * 从字节流中解析出调用命令对象
     * tag|cluster|targetId|methodName|parameters...
     *
     * @param callerId 调用者标志符
     * @param bytes 字节数组
     * @return 调用命令对象
     */
    public static Command build(int callerId, byte[] bytes) {
        Command result = null;
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            result = new Command();
            result.callerId = callerId;
            result.tag = stream.readInt();
            result.cluster = (String) stream.readObject();
            result.targetId = stream.readObject();
            result.method = new CommandMethod();
            result.method.method = (String) stream.readObject();
            ArrayList<Object> parameterList = new ArrayList<Object>();
            while(stream.available() != -1) {
                parameterList.add(stream.readObject());
            }
            result.method.parameters = parameterList.toArray();
        }
        catch(Exception ex) {
            logger.error("Command.build(?, ?) execute failed", ex);
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
     * 将调用命令对象转化为字节数组
     * tag|cluster|targetId|methodName|parameters...
     *
     * @return 字节数组
     */
    public byte[] toBytes() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = null;
        try {
            objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeInt(tag);
            objectStream.writeObject(cluster);
            objectStream.writeObject(targetId);
            objectStream.writeObject(method.method);
            for(Object object : method.parameters) {
                objectStream.writeObject(object);
            }
        }
        catch(Exception ex) {
            logger.error("Command.toBytes() execute failed", ex);
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
