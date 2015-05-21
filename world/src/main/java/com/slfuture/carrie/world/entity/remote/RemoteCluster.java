package com.slfuture.carrie.world.entity.remote;

import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.world.Cluster;

/**
 * 远程簇
 */
public class RemoteCluster extends Cluster {
    /**
     * 接口类名
     */
    public String interfaceName;


    /**
     * 构建远程簇
     *
     * @param conf 配置对象
     * @return 簇
     */
    public static RemoteCluster build(IXMLNode conf) {
        String classString = conf.get("interface");
        Class<?> clazz = null;
        try {
            clazz = Class.forName(classString);
        }
        catch(ClassNotFoundException ex) {
            return null;
        }
        RemoteCluster result = new RemoteCluster();
        result.name = conf.get("name");
        result.interfaceName = conf.get("class");
        result.manager = RemoteManager.build(result, conf.visit("manager"));
        return result;
    }
}
