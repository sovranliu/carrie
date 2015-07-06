package com.dianping.midasx.utility;

import com.dianping.midasx.base.io.net.NetEntry;
import com.dianping.midasx.utility.net.TCPConnection;
import com.dianping.midasx.utility.net.TCPEntry;
import org.apache.log4j.PropertyConfigurator;

/**
 * 测试
 */
public class Test {
    public static class ServerTCPEntry extends TCPEntry {

        /**
         * 目标状态变化
         *
         * @param target 目标
         * @param status 新状态
         */
        @Override
        public void onStatusChanged(TCPConnection target, int status) {
            System.out.println("ServerTCPEntry.onStatusChanged(" + status + ")");
            byte[] data = {1 ,2 ,3, 4};
            try {
                target.write(data);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        /**
         * 监听回调
         *
         * @param target 目标
         * @param data   内容
         */
        @Override
        public void onRead(TCPConnection target, byte[] data) {
            System.out.println("ServerTCPEntry.onRead" + data);
            byte[] aa = {1 ,2 ,3, 4};
            try {
                target.write(aa);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static class ClientTCPConnection extends TCPConnection {
        /**
         * 状态变化回调
         *
         * @param status 新状态
         */
        @Override
        public void onStatusChanged(int status) {
            if(TCPConnection.STATUS_CONNECTED == status) {
                System.out.println("ClientTCPConnection.Successful(" + status + ")");
                byte[] data = {1 ,2 ,3, 4};
                try {
                    this.write(data);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("ClientTCPConnection.onStatusChanged(" + status + ")");
        }

        /**
         * 监听回调
         *
         * @param data 内容
         */
        @Override
        public void onRead(byte[] data) {
            System.out.println("ClientTCPConnection.onRead(" + data + ")");
        }
    }


    public static abstract class C {
        public C(int i) {}

        public abstract void on();
    }

    /**
     * 主函数
     */
    public static void main(String[] argv) throws Exception {
        C c = new C(1) {
            public  void on() {}
        };

        PropertyConfigurator.configure("/F:/log4j.properties");
        TCPEntry server = new ServerTCPEntry();
        server.ip = "127.0.0.1";
        server.port = 9527;
        System.out.println(server.open());

        NetEntry remote = new NetEntry();
        remote.ip = "127.0.0.1";
        remote.port = 9527;
        ClientTCPConnection connection = new ClientTCPConnection();
        System.out.println(connection.connect(remote));
        Thread.sleep(1000);
        byte[] data = {1 ,2 ,3, 4};
        connection.write(data);
        Thread.sleep(1000000);
    }
}
