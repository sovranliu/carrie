package com.slfuture.carrie.utility.config.zookeeper;

import com.slfuture.carrie.base.json.JSONObject;
import com.slfuture.carrie.base.json.core.IJSON;
import com.slfuture.carrie.base.model.Path;
import com.slfuture.carrie.base.text.Text;
import com.slfuture.carrie.base.type.Set;
import com.slfuture.carrie.base.type.core.ILink;
import com.slfuture.carrie.base.type.core.ISet;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.utility.config.core.IConfigWatcher;
import com.slfuture.carrie.utility.config.core.IRootConfig;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

/**
 * 根配置对象
 */
public class RootConfig extends Config implements IRootConfig {
    /**
     * Zookeeper超时时间
     */
    public final static int ZOOKEEPER_TIMEOUT = 3000;
    /**
     * Zookeeper节点
     */
    protected ZooKeeper node = null;
    /**
     * 路径
     */
    protected Path path = null;
    /**
     * 监视者集合
     */
    protected ISet<IConfigWatcher> watchers = new Set<IConfigWatcher>();


    /**
     * 构造函数
     */
    public RootConfig() { }

    /**
     * 加载根配置
     *
     * @param uri 路径
     * @return 是否执行成功
     */
    @Override
    public boolean load(String uri) {
        int i = uri.indexOf("/");
        if(-1 == i) {
            logger.error("path missing when connecting znode");
            return false;
        }
        path = new Path(uri.substring(i), "/");
        uri = uri.substring(0, i);
        try {
            node = new ZooKeeper(uri, ZOOKEEPER_TIMEOUT, null);
        }
        catch (IOException e) {
            logger.error("ZooKeeper node create failed", e);
            return false;
        }
        // 递归构建子节点
        Stack<Config> confStack = new Stack<Config>();
        Stack<String> pathStack = new Stack<String>();
        confStack.push(this);
        pathStack.push("/" + path.toString("/"));
        while(true) {
            if(confStack.isEmpty()) {
                break;
            }
            Config currentConfig = confStack.pop();
            String currentPath = pathStack.pop();
            try {
                String data = new String(node.getData(currentPath, false, null), "UTF-8");
                if(!Text.isBlank(data)) {
                    JSONObject jObject = JSONObject.convert(data);
                    for(ILink<String, IJSON> link : jObject) {
                        currentConfig.properties.put(link.getOrigin(), link.getDestination().toString());
                    }
                }
                List<String> childrenName = node.getChildren("/" + path.toString("/"), null);
                for(String childName : childrenName) {
                    i = childName.indexOf("#");
                    String name = null;
                    if(-1 == i) {
                        name = childName;
                    }
                    else {
                        name = childName.substring(0, i);
                    }
                    Config childConfig = new Config();
                    ISet<IConfig> childConfigSet = currentConfig.children.get(name);
                    if(null == childConfigSet) {
                        childConfigSet = new Set<IConfig>();
                        currentConfig.children.put(name, childConfigSet);
                    }
                    childConfigSet.add(childConfig);
                    confStack.push(childConfig);
                    pathStack.push(currentPath + "/" + childName);
                }
            }
            catch (Exception e) {
                logger.error("fatch Zookeeper children failed", e);
            }
        }
        return true;
    }

    /**
     * 监听配置变化
     *
     * @param watcher 监视者
     * @throws UnsupportedOperationException 部分配置不支持该操作
     */
    @Override
    public void watch(IConfigWatcher watcher) throws UnsupportedOperationException {
        if(0 == watchers.size()) {
            try {
                node.exists("/" + path.toString("/"), new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        if(Event.EventType.None != event.getType()) {
                            for(IConfigWatcher watcher : watchers) {
                                try {
                                    watcher.onChanged();
                                }
                                catch(Exception e) {
                                    logger.error("config watch callback failed", e);
                                }
                            }
                            return;
                        }
                        try {
                            node.exists("/" + path.toString("/"), this);
                        }
                        catch (Exception e) {
                            logger.error("watch zookeeper failed", e);
                        }
                    }
                });
            }
            catch (Exception e) {
                logger.error("watch zookeeper failed", e);
            }
        }
        for(IConfigWatcher confWatch : watchers) {
            if(confWatch == watcher) {
                return;
            }
        }
        watchers.add(watcher);
    }
}
