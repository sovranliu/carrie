package com.slfuture.carrie.utility.net;

import com.slfuture.carrie.base.interaction.core.ISession;
import com.slfuture.carrie.base.io.net.NetEntry;
import com.slfuture.carrie.base.model.core.IHandle;
import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.base.type.core.IMapping;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

/**
 * TCP入口
 */
public abstract class TCPEntry extends NetEntry implements IHandle, ISession<TCPConnection, byte[]>, IMapping<Integer, TCPConnection> {
    /**
     * 服务端连接
     */
    public class ServerTCPConnection extends TCPConnection {

        /**
         * 状态变化回调
         *
         * @param status 新状态
         */
        @Override
        public void onStatusChanged(int status) {
            if(STATUS_DISCONNECTED == status) {
                connections.delete(local().port);
            }
            TCPEntry.this.onStatusChanged(this, status);
        }

        /**
         * 监听回调
         *
         * @param data 内容
         */
        @Override
        public void onRead(byte[] data) {
            TCPEntry.this.onRead(this, data);
        }
    }


    /**
     * 协调事件轮询组
     */
    protected EventLoopGroup syncGroup = null;
    /**
     * IO事件轮询组
     */
    protected EventLoopGroup ioGroup = null;
    /**
     * 引导器
     */
    protected ServerBootstrap bootstrap = null;
    /**
     * 关联的连接表
     */
    protected Table<Integer, ServerTCPConnection> connections = new Table<Integer, ServerTCPConnection>();
    /**
     * 日志对象
     */
    protected static Logger logger = Logger.getLogger(TCPEntry.class);


    /**
     * 打开句柄
     *
     * @return 是否打开成功
     */
    @Override
    public boolean open() {
        syncGroup = new NioEventLoopGroup();
        ioGroup = new NioEventLoopGroup();
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(syncGroup, ioGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ServerTCPConnection connection = new ServerTCPConnection();
                    connection.local = new NetEntry();
                    connection.local.ip = ch.localAddress().getAddress().getHostAddress();
                    connection.local.port = ch.localAddress().getPort();
                    connection.remote = new NetEntry();
                    connection.remote.ip = ch.remoteAddress().getAddress().getHostAddress();
                    connection.remote.port = ch.remoteAddress().getPort();
                    connections.put(ch.localAddress().getPort(), connection);
                    ch.pipeline().addLast(connection.tcpAdapter);
                }
            }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.bind(ip, port).sync();
        }
        catch(Exception ex ) {
            logger.error("TCPEntry.open() execute failed", ex);
            return false;
        }
        return true;
    }

    /**
     * 关闭句柄
     */
    @Override
    public void close() {
        connections.clear();
        syncGroup.shutdownGracefully();
        ioGroup.shutdownGracefully();
    }

    /**
     * 写入
     *
     * @param target 目标
     * @param data   数据
     */
    @Override
    public void write(TCPConnection target, byte[] data) throws Exception {
        target.write(data);
    }

    /**
     * 获取指定参数对应值
     *
     * @param key 参数
     * @return 值，若值不存在则返回null
     */
    @Override
    public TCPConnection get(Integer key) {
        return connections.get(key);
    }

    /**
     * 目标状态变化
     *
     * @param target 目标
     * @param status 新状态
     */
    public abstract void onStatusChanged(TCPConnection target, int status);
}
