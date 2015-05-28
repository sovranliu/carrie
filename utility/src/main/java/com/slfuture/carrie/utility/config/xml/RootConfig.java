package com.slfuture.carrie.utility.config.xml;

import com.slfuture.carrie.base.xml.XMLDocument;
import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.utility.config.core.IConfigWatcher;
import com.slfuture.carrie.utility.config.core.IRootConfig;

import java.io.File;

/**
 * 根配置对象
 */
public class RootConfig extends Config implements IRootConfig {
    /**
     * 构造函数
     */
    public RootConfig() { }

    /**
     * 构造函数
     *
     * @param node XML配置节点
     */
    protected RootConfig(IXMLNode node) {
        super(node);
    }

    /**
     * 加载根配置
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
        this.node = document.content();
        document.close();
        return true;
    }
}
