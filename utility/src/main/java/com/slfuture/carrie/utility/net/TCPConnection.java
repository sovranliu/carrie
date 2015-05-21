package com.slfuture.carrie.utility.net;

import com.slfuture.carrie.base.interaction.core.IConnection;
import com.slfuture.carrie.base.io.net.NetEntry;
import com.slfuture.carrie.base.logic.State;
import com.slfuture.carrie.base.logic.StateMachine;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * TCP连接对象
 */
public abstract class TCPConnection implements IConnection<NetEntry, byte[]> {
    /**
     * 连接状态：断开
     */
    public final static int STATUS_DISCONNECTED = 0;
    /**
     * 连接状态：正在连接
     */
    public final static int STATUS_CONNECTING = 1;
    /**
     * 连接状态：已连接
     */
    public final static int STATUS_CONNECTED = 2;
    /**
     * 连接状态：正在断开
     */
    public final static int STATUS_DISCONNECTING = 3;

    /**
     * 动作：尝试连接
     */
    public final static int ACT_CONNECT = 1;
    /**
     * 动作：确认状态
     */
    public final static int ACT_CONFIRM_CONNECTED = 2;
    /**
     * 动作：接受连接
     */
    public final static int ACT_ACCEPT = 3;
    /**
     * 动作：断开连接
     */
    public final static int ACT_DISCONNECT = 4;
    /**
     * 动作：确认状态
     */
    public final static int ACT_CONFIRM_DISCONNECTED = 5;


    /**
     * TCP客户端处理句柄
     */
    public class TCPAdapter extends ChannelHandlerAdapter {
        @Override
        public void channelActive(final ChannelHandlerContext ctx) {
            currentContext = ctx;
            try {
                if(STATUS_CONNECTING == stateMachine.status()) {
                    stateMachine.write(ACT_CONFIRM_CONNECTED);
                }
                else if(STATUS_DISCONNECTED == stateMachine.status()) {
                    stateMachine.write(ACT_ACCEPT);
                }
                TCPConnection.this.local = new NetEntry();
                TCPConnection.this.local.ip = ((InetSocketAddress) ctx.channel().localAddress()).getAddress().getHostAddress();
                TCPConnection.this.local.port = ((InetSocketAddress) ctx.channel().localAddress()).getPort();
                TCPConnection.this.remote = new NetEntry();
                TCPConnection.this.remote.ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
                TCPConnection.this.remote.port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
                onStatusChanged(stateMachine.status());
            }
            catch (Exception ex) {
                logger.error("error occurred during tcp connection established", ex);
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            currentContext = ctx;
            ByteBuf buffer = (ByteBuf) msg;
            try {
                byte[] data = new byte[buffer.readableBytes()];
                buffer.readBytes(data);
                onRead(data);
            }
            catch (Exception ex) {
                logger.error("TCPClientHandler.channelRead() execute failed", ex);
            }
            finally {
                buffer.release();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            try {
                stateMachine.write(ACT_DISCONNECT);
                onStatusChanged(stateMachine.status());
                ctx.close();
                stateMachine.write(ACT_CONFIRM_DISCONNECTED);
                onStatusChanged(stateMachine.status());
            }
            catch (Exception ex) {
                logger.error("error occurred during tcp connection exception caught", ex);
            }
        }
    }

    /**
     * 本地入口
     */
    protected NetEntry local = null;
    /**
     * 远程入口
     */
    protected NetEntry remote = null;
    /**
     * IO事件轮询组
     */
    protected EventLoopGroup ioGroup = null;
    /**
     * 引导器
     */
    protected Bootstrap bootstrap = null;
    /**
     * TCP句柄
     */
    TCPAdapter tcpAdapter = null;
    /**
     * 当前上下文
     */
    protected ChannelHandlerContext currentContext = null;
    /**
     * 状态图
     */
    protected static State<Integer, Integer> stateChart = null;
    /**
     * 状态机
     */
    protected StateMachine<Integer, Integer> stateMachine = null;
    /**
     * 日志对象
     */
    protected static Logger logger = Logger.getLogger(TCPConnection.class);


    /**
     * 构造函数
     */
    public TCPConnection() {
        if(null == stateChart) {
            synchronized (TCPConnection.class) {
                if(null == stateChart) {
                    Integer[] values = {STATUS_DISCONNECTED, STATUS_CONNECTING, STATUS_CONNECTING, STATUS_CONNECTED, STATUS_DISCONNECTED, STATUS_CONNECTED, STATUS_CONNECTED, STATUS_DISCONNECTING, STATUS_DISCONNECTING, STATUS_DISCONNECTED};
                    Integer[] inputs = {ACT_CONNECT, ACT_CONFIRM_CONNECTED, ACT_ACCEPT, ACT_DISCONNECT, ACT_CONFIRM_DISCONNECTED};
                    stateChart = State.<Integer, Integer>build(values, inputs);
                }
            }
        }
        stateMachine = new StateMachine<Integer, Integer>(stateChart);
        tcpAdapter = new TCPAdapter();
    }

    /**
     * 连接
     *
     * @param target 目标
     * @return 连接是否成功
     */
    @Override
    public boolean connect(NetEntry target) throws Exception {
        stateMachine.write(ACT_CONNECT);
        onStatusChanged(stateMachine.status());
        ioGroup = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(ioGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(tcpAdapter);
                }
            });
            bootstrap.connect(target.ip, target.port);
        }
        catch(Exception ex) {
            logger.error("TCPClientHandler.connect(" + target + ") execute failed", ex);
            return false;
        }
        return true;
    }

    /**
     * 断开
     */
    @Override
    public void disconnect() {
        try {
            stateMachine.write(ACT_DISCONNECT);
            onStatusChanged(stateMachine.status());
            if(null != currentContext) {
                currentContext.close();
                currentContext = null;
            }
            if(null != ioGroup) {
                ioGroup.shutdownGracefully();
                ioGroup = null;
            }
            stateMachine.write(ACT_CONFIRM_DISCONNECTED);
            onStatusChanged(stateMachine.status());
        }
        catch (Exception ex) {
            logger.error("error occurred during tcp connection disconnect", ex);
        }
    }

    /**
     * 本地
     *
     * @return 本地
     */
    @Override
    public NetEntry local() {
        return local;
    }

    /**
     * 获取对方
     *
     * @return 对方对象
     */
    @Override
    public NetEntry remote() {
        return remote;
    }

    /**
     * 当前状态
     *
     * @return 状态
     */
    @Override
    public Integer status() {
        return stateMachine.status();
    }

    /**
     * 写入
     *
     * @param data 数据
     */
    @Override
    public void write(byte[] data) throws Exception {
        ByteBuf buffer = currentContext.alloc().buffer(data.length);
        buffer.writeBytes(data);
        currentContext.writeAndFlush(buffer);
    }

    /**
     * 转化为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return local() + " - " + remote();
    }
}
