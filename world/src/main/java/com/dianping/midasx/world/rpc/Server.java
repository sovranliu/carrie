package com.dianping.midasx.world.rpc;

import com.dianping.midasx.base.async.PipeLine;
import com.dianping.midasx.base.async.core.IOperation;
import com.dianping.midasx.base.type.core.IMap;
import com.dianping.midasx.base.type.safe.Table;
import com.dianping.midasx.utility.net.TCPConnection;
import com.dianping.midasx.utility.net.TCPEntry;
import com.dianping.midasx.world.IObject;
import com.dianping.midasx.world.World;
import com.dianping.midasx.world.logic.Command;
import com.dianping.midasx.world.logic.Result;
import com.dianping.midasx.world.rpc.logic.Protocol;

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
            IObject target = World.get(command.cluster, command.targetId, IObject.class);
            try {
                Object result = target.invoke(command.method, command.parameters);
                TCPConnection connection = connections.get(command.callerId);
                if(null == connection) {
                    logger.error("connection loss when call finished");
                    return null;
                }
                connection.write(Protocol.convert((new Result(command.cluster, command.tag, result)).toBytes()));
            }
            catch (Exception e) {
                logger.error("entity invoke method failed ; " + command.method.name);
                return null;
            }
            return null;
        }
    }


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
                operation.command = Command.build(target.local().port, bytes);
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
