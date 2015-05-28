package com.slfuture.carrie.world.rpc.logic;

import com.slfuture.carrie.base.model.GlobeIdentity;
import com.slfuture.carrie.world.rpc.Method;

import java.io.*;
import java.util.ArrayList;

/**
 * RPC 命令
 *
 * token.name.condition.method.parameters
 */
public class Command {
    /**
     * 返回值处理管理器
     */
    public final static String NAME_RETURN = "return";
    /**
     * 返回值处理管理器方法
     */
    public final static String METHOD_RETURN = "call";


    /**
     * 连接本地端口
     */
    public int port;
    /**
     * 口令
     */
    public int token;
    /**
     * 管理器名称
     */
    public String name;
    /**
     * 条件
     */
    public GlobeIdentity<Object> condition;
    /**
     * 方法名称
     */
    public Method method;
    /**
     * 参数列表
     */
    public Object[] parameters;


    /**
     * 从字节数组构建RPC命令
     *
     * @param bytes 字节数组
     * @return RPC命令
     */
    public static Command build(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Command result = new Command();

        result.token = stream.readInt();
        result.name = (String) stream.readObject();
        result.condition = (GlobeIdentity<Object>) stream.readObject();
        result.method = (Method) stream.readObject();
        ArrayList<Object> objectList = new ArrayList<Object>();
        while(stream.available() != -1) {
            objectList.add(stream.readObject());
        }
        result.parameters = objectList.toArray();
        stream.close();
        return result;
    }

    /**
     * 构建执行结果口令
     *
     * @param token 口令
     * @param result 结果
     * @return 执行结果命令
     */
    public static Command build(int token, Object result) {
        Command command = new Command();
        command.token = token;
        command.name = NAME_RETURN;
        command.method = new Method();
        command.method.name = METHOD_RETURN;
        command.method.parameterTypes = new Class<?>[1];
        command.method.parameterTypes[0] = Object.class;
        command.parameters = new Object[1];
        command.parameters[0] = result;
        return command;
    }

    /**
     * 转化为字节数组
     *
     * @return 字节数组
     */
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(token);
        objectStream.writeObject(name);
        objectStream.writeObject(condition);
        objectStream.writeObject(method);
        for(Object object : parameters) {
            objectStream.writeObject(object);
        }
        objectStream.close();
        return byteStream.toByteArray();
    }
}
