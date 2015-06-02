package com.slfuture.carrie.utility.config;

import com.slfuture.carrie.base.xml.XMLDocument;
import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.utility.config.core.IConfig;
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
    private static Logger logger = Logger.getLogger(Configuration.class);
    /**
     * 根节点
     */
    private static IRootConfig root = null;


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
                IRootConfig conf = (IRootConfig) Class.forName(node.get("class")).newInstance();
                if(!conf.load(node.get("uri"))) {
                    logger.error("root config load failed:\n" + node.toString());
                    continue;
                }
                if(null == root) {
                    root = conf;
                }
                else {
                    root.attach(node.get("path"), conf);
                }
            }
            catch(Exception ex) {
                logger.error("root config build failed:\n" + node.toString(), ex);
            }
        }
        return true;
    }

    /**
     * 获取根节点
     *
     * @return 配置对象
     */
    public static IConfig root() {
        return root;
    }
}
