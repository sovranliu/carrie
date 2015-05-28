package com.slfuture.carrie.world.rpc;

import com.slfuture.carrie.base.async.PipeLine;
import com.slfuture.carrie.base.async.core.IOperation;
import com.slfuture.carrie.base.type.core.IMap;
import com.slfuture.carrie.base.type.core.ITable;
import com.slfuture.carrie.base.type.safe.Table;
import com.slfuture.carrie.utility.net.TCPConnection;
import com.slfuture.carrie.utility.net.TCPEntry;
import com.slfuture.carrie.world.rpc.core.IEntityManager;
import com.slfuture.carrie.world.rpc.logic.Command;
import com.slfuture.carrie.world.rpc.logic.Protocol;

import java.lang.reflect.Method;

/**
 * 远程调用服务方
 */
public class Server extends TCPEntry {
    /**
     * 调用操作
     */
    public class InvokeOperation implements IOperation<Void> {
        /**
         * 调用命令
         */
        public Command command;


        /**
         * 操作结束回调
         *
         * @return 操作结果
         */
        @Override
        public Void onExecute() {
            IEntityManager manager = managerTable.get(command.name);
            if(null == manager) {
                logger.error("can not find entity manager : " + command.name);
                return null;
            }
            Object object = manager.load(command.condition);
            if(null == object) {
                logger.error("can not find entity : " + command.condition);
                return null;
            }
            try {
                Method method = object.getClass().getDeclaredMethod(command.method.name, command.method.parameterTypes);
                Object result = method.invoke(object, command.parameters);
                TCPConnection connection = connections.get(command.port);
                if(null == connection) {
                    logger.error("connection loss when call finished");
                    return null;
                }
                connection.write(Protocol.convert(Command.build(command.token, result).toBytes()));
            }
            catch (Exception e) {
                logger.error("entity invoke method failed ; " + command.method.name);
                return null;
            }
            return null;
        }
    }


    /**
     * 管理器映射
     */
    public ITable<String, IEntityManager> managerTable = new Table<String, IEntityManager>();
    /**
     * 协议栈映射
     */
    public IMap<Integer, Protocol> protocols = new Table<Integer, Protocol>();
    /**
     * 流水线
     */
    public static PipeLine pipeLine = null;


    /**
     * 获取流水线对象
     *
     * @return 流水线对象
     */
    private PipeLine getPipeLine() {
        if(null == pipeLine) {
            synchronized(Server.class) {
                if(null == pipeLine) {
                    pipeLine = new PipeLine();
                    pipeLine.start(20, 100);
                }
            }
        }
        return pipeLine;
    }

    /**
     * 注册/替代实体管理器
     *
     * @param name 注册名
     * @param entityManager 实体管理器,若为null表示解除注册名
     */
    public void register(String name, IEntityManager entityManager) {

    }

    /**
     * 目标状态变化
     *
     * @param target 目标
     * @param status 新状态
     */
    @Override
    public void onStatusChanged(TCPConnection target, int status) {
        if(TCPConnection.STATUS_CONNECTED == status) {
            protocols.put(target.local().port, new Protocol());
        }
        else if(TCPConnection.STATUS_DISCONNECTED == status) {
            protocols.delete(target.local().port);
        }
        logger.info(target.toString() + " STATUS = " + status);
    }

    /**
     * 监听回调
     *
     * @param target 目标
     * @param data   内容
     */
    @Override
    public void onRead(TCPConnection target, byte[] data) {
        Protocol protocol = protocols.get(target.local().port);
        if(null == protocol) {
            logger.error("read on no protocol port : " + target.local().port);
            target.disconnect();
            return;
        }
        byte[] bytes = protocol.filter(data);
        while(null != bytes) {
            InvokeOperation operation = new InvokeOperation();
            try {
                operation.command = Command.build(bytes);
                operation.command.port = target.local().port;
                getPipeLine().supply(operation);
            }
            catch (Exception e) {
                logger.error("command invoke failed", e);
                return;
            }
            bytes = protocol.filter();
        }
    }
}
