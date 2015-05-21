package com.slfuture.carrie.world.entity.local;

import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.world.Cluster;
import com.slfuture.carrie.world.IManager;

/**
 * 本地簇类
 */
public class LocalCluster extends Cluster {
    /**
     * 接口类名
     */
    public String interfaceName;
    /**
     * 实现类名
     */
    public String className;


    /**
     * 构建本地簇
     *
     * @param conf 配置对象
     * @return 簇
     */
    public static LocalCluster build(IXMLNode conf) {
        LocalCluster result = new LocalCluster();
        result.name = conf.get("name");
        result.interfaceName = conf.get("interface");
        result.className = conf.get("class");
        Class<?> clazz = null;
        try {
            clazz = Class.forName(result.className);
        }
        catch(ClassNotFoundException ex) {
            return null;
        }
        conf = conf.visit("manager");
        if(null == conf) {
            return null;
        }
        try {
            clazz = Class.forName(conf.get("class"));
        }
        catch(ClassNotFoundException ex) {
            return null;
        }
        try {
            result.manager = (IManager<?>) clazz.newInstance();
        }
        catch (InstantiationException e) {
            return null;
        }
        catch (IllegalAccessException e) {
            return null;
        }
        return result;
    }
}
