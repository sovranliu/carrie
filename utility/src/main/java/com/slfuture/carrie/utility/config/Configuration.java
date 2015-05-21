package com.slfuture.carrie.utility.config;

import com.slfuture.carrie.base.type.safe.Table;
import com.slfuture.carrie.base.xml.XMLDocument;
import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.utility.config.core.IRootConfig;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * 配置门面对象
 *
 * 系统配置访问的入口类
 */
public class Configuration {
    /**
     * 日志对象
     */
    protected static Logger logger = Logger.getLogger(Configuration.class);
    /**
     * 键与根配置的映射
     */
    private static Table<String, IRootConfig> configTable = new Table<String, IRootConfig>();


    /**
     * 隐藏构造函数
     */
    private Configuration() { }

    /**
     * 初始化配置
     *
     * @param path 配置文件路径
     * @return 执行结果
     */
    public static boolean build(String path) {
        XMLDocument document = new XMLDocument();
        if(!document.open(new File(path))) {
            logger.error("Configuration initialize file open failed:" + path);
            return false;
        }
        return build(document.content());
    }

    /**
     * 初始化配置
     *
     * @param xml XML配置对象
     * @return 执行结果
     */
    public static boolean build(IXMLNode xml) {
        for(IXMLNode node : xml.visits("root")) {
            try {
                IRootConfig root = (IRootConfig) Class.forName(node.get("class")).newInstance();
                if(!root.load(node.get("uri"))) {
                    logger.error("root config load failed:\n" + node.toString());
                    continue;
                }
                configTable.put(node.get("name"), root);
            }
            catch(Exception ex) {
                logger.error("root config build failed:\n" + node.toString(), ex);
            }
        }
        return true;
    }

    /**
     * 获取指定名称的配置对象
     *
     * @param name 配置名称
     * @return 配置对象
     */
    public static IRootConfig get(String name) {
        return configTable.get(name);
    }
}
