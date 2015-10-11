package com.slfuture.carrie.utility.config.xml;

import com.slfuture.carrie.base.type.Set;
import com.slfuture.carrie.base.type.core.ILink;
import com.slfuture.carrie.base.type.core.ISet;
import com.slfuture.carrie.base.xml.XMLDocument;
import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.utility.config.core.IRootConfig;

import java.io.File;

/**
 * XML根配置类
 */
public class RootConfig extends Config implements IRootConfig {
    /**
     * 加载指定路径的配置文件
     *
     * @param uri 路径
     * @return 是否执行成功
     */
    @Override
    public boolean load(String uri) {
        XMLDocument document = new XMLDocument();
        if(!document.open(new File(uri))) {
            return false;
        }
        for(ILink<String, String> link : document.content()) {
            properties.put(link.origin(), link.destination());
        }
        for(IXMLNode child : document.content().children()) {
            ISet<IConfig> configSet = children.get(child.getName());
            if(null == configSet) {
                configSet = new Set<IConfig>();
                children.put(child.getName(), configSet);
            }
            configSet.add(convert(child));
        }
        document.close();
        return true;
    }

    /**
     * 附加配置节点
     *
     * @param path 节点路径
     * @param conf 配置节点
     */
    @Override
    public boolean attach(String path, IConfig conf) {
        int i = path.lastIndexOf("/");
        if(-1 == i) {
            this.children.put(path, new Set<IConfig>(conf));
            return true;
        }
        ((Config) (visit(path.substring(0, i)))).children.put(path.substring(i + 1), new Set<IConfig>(conf));
        return true;
    }

    /**
     * XML节点转换配置节点
     *
     * @param node XML节点
     * @return 配置节点
     */
    public static RootConfig convert(IXMLNode node) {
        RootConfig result = new RootConfig();
        for(ILink<String, String> link : node) {
            result.properties.put(link.origin(), link.destination());
        }
        result.value = node.getValue();
        for(IXMLNode child : node.children()) {
            ISet<IConfig> configSet = result.children.get(child.getName());
            if(null == configSet) {
                configSet = new Set<IConfig>();
                result.children.put(child.getName(), configSet);
            }
            configSet.add(convert(child));
        }
        return result;
    }
}
