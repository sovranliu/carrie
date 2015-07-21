package com.dianping.midasx.world.rpc;

import com.dianping.midasx.base.etc.Serial;
import com.dianping.midasx.base.io.net.NetEntry;
import com.dianping.midasx.base.model.Method;
import com.dianping.midasx.base.time.Time;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.ILink;
import com.dianping.midasx.base.type.core.IMap;
import com.dianping.midasx.base.type.core.ITable;
import com.dianping.midasx.base.type.safe.Table;
import com.dianping.midasx.utility.net.TCPConnection;
import com.dianping.midasx.world.logic.Command;
import com.dianping.midasx.world.logic.Result;
import com.dianping.midasx.world.rpc.logic.Protocol;
import org.apache.log4j.Logger;

import java.util.Random;

/**
 * 通信客户端类
 */
public abstract class Client {
    /**
     * 客户端TCP连接类
     */
    public class ClientTCPConnection extends TCPConnection {
        /**
         * 状态变化回调
         *
         * @param status 新状态
         */
        @Override
        public void onStatusChanged(int status) {
            if(TCPConnection.STATUS_DISCONNECTED == status) {
                connections.delete(ClientTCPConnection.this.remote().toString());
            }
            else if(TCPConnection.STATUS_CONNECTED == status) {
                connections.put(ClientTCPConnection.this.remote().toString(), ClientTCPConnection.this);
            }
            logger.info(ClientTCPConnection.this.toString() + " STATUS = " + status);
        }

        /**
         * 监听回调
         *
         * @param data 内容
         */
        @Override
        public void onRead(byte[] data) {
            Protocol protocol = protocols.get(this.local().port);
            if(null == protocol) {
                logger.error("read on no protocol port : " + this.local().port);
                this.disconnect();
                return;
            }
            byte[] bytes = protocol.filter(data);
            while(null != bytes) {
                Result result = Result.build(bytes);
                Command command = (Command) commandTable.get(result.tag);
                commandTable.put(result.tag, result);
                synchronized (command) {
                    command.notify();
                }
                bytes = protocol.filter();
            }
        }
    }


    /**
     * 日志对象
     */
    protected static Logger logger = Logger.getLogger(Client.class);
    /**
     * 连接池
     */
    public Table<String, ClientTCPConnection> connections = new Table<String, ClientTCPConnection>();
    /**
     * 调用映射
     */
    private ITable<Integer, Object> commandTable = new Table<Integer, Object>();
    /**
     * 协议栈映射
     */
    public IMap<Integer, Protocol> protocols = new Table<Integer, Protocol>();


    /**
     * 初始化
     *
     * @param entrySet 入口集合
     * @return 执行结果
     */
    public boolean initailize(ICollection<NetEntry> entrySet) {
        for(NetEntry entry : entrySet) {
            if(null == connections.get(entry.toString())) {
                ClientTCPConnection connection = new ClientTCPConnection();
                try {
                    if(!connection.connect(entry)) {
                        continue;
                    }
                    connections.put(entry.toString(), connection);
                }
                catch (Exception e) {
                    logger.error("ClientTCPConnection connect " + entry.toString() + " failed", e);
                }
            }
        }
        for(ILink<String, ClientTCPConnection> link : connections) {
            boolean sentry = false;
            for(NetEntry entry : entrySet) {
                if(link.destination().remote().equals(entry)) {
                    sentry = true;
                    break;
                }
            }
            if(!sentry) {
                link.destination().disconnect();
                connections.delete(link.origin());
            }
        }
        return true;
    }

    /**
     * 析构
     */
    public void terminate() {
        for(ILink<String, ClientTCPConnection> link : connections) {
            link.destination().disconnect();
        }
        connections.clear();
        protocols.clear();
        commandTable.clear();
    }

    /**
     * 调用本应用的方法
     *
     * @param clusterName 被调用对象标志
     * @param id 被调用对象标志符
     * @param method 调用方法
     * @param parameters 调用参数
     * @return 调用返回值
     */
    public Object invoke(String clusterName, Object id, Method method, Object...parameters) {
        if(0 == connections.size()) {
            throw new RuntimeException("No valid connection");
        }
        Random random = new Random(Time.now().millis());
        int index = random.nextInt(connections.size()), i = 0;
        ClientTCPConnection connection = null;
        for(ILink<String, ClientTCPConnection> link : connections) {
            if(i == index) {
                connection = link.destination();
                break;
            }
        }
        Command command = new Command();
        command.cluster = clusterName;
        command.targetId = id;
        command.method = method;
        command.parameters = parameters;
        command.tag = Serial.makeLoopInteger();
        commandTable.put(command.tag, command);
        try {
            connection.write(command.toBytes());
        }
        catch (Exception e) {
            commandTable.delete(command.tag);
            throw new RuntimeException(e);
        }
        try {
            synchronized (command) {
                command.wait(10000);
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Result result = (Result) commandTable.get(command.tag);
        commandTable.delete(command.tag);
        return result.result;
    }
}
