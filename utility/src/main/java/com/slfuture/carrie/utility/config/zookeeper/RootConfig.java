package com.slfuture.carrie.utility.config.zookeeper;

import com.slfuture.carrie.base.json.JSONObject;
import com.slfuture.carrie.base.json.core.IJSON;
import com.slfuture.carrie.base.text.Text;
import com.slfuture.carrie.base.type.Set;
import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.base.type.core.ILink;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.utility.config.core.IRootConfig;

import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * 根配置对象
 */
public class RootConfig extends Config implements IRootConfig {
    /**
     * Zookeeper超时时间
     */
    public final static int ZOOKEEPER_TIMEOUT = 3000;
    /**
     * 分隔符
     */
    public final static String ZOOKEEPER_SEPARATOR = "#";
    /**
     * Zookeeper节点
     */
    protected ZooKeeper node = null;
    /**
     * Zookeeper路径
     */
    protected String path = null;


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
        path = uri.substring(i);
        uri = uri.substring(0, i);
        try {
            node = new ZooKeeper(uri, ZOOKEEPER_TIMEOUT, null);
        }
        catch (Exception e) {
            logger.error("access zookeeper failed", e);
        }
        return refresh(path, this);
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
        ((com.slfuture.carrie.utility.config.Config) (visit(path.substring(0, i)))).children.put(path.substring(i + 1), new Set<IConfig>(conf));
        return true;
    }

    /**
     * 获取指定路径的Zookeeper节点保存的配置信息
     *
     * @param relativePath 相对路径
     * @return 配置信息
     */
    public Config zget(String relativePath) {
        Config result = new Config();
        if(refresh(path + "/" + relativePath, result)) {
            return result;
        }
        return null;
    }

    /**
     * 获取指定路径的Zookeeper节点保存的配置信息集合
     *
     * @param relativePath 相对路径
     * @return 配置信息集合
     */
    public ICollection<IConfig> zgets(String relativePath) {
        int i = relativePath.lastIndexOf("/");
        if(-1 == i) {
            return new Set<IConfig>();
        }
        String mainName = relativePath.substring(i + 1);
        Set<IConfig> result = new Set<IConfig>();
        try {
            List<String> childrenName = node.getChildren(path + "/" + relativePath.substring(0, i), null);
            for(String childName : childrenName) {
                if(0 == childName.indexOf(mainName)) {
                    Config conf = new Config();
                    conf.name = childName;
                    if(!refresh(path + "/" + relativePath.substring(0, i) + childName, conf)) {
                        logger.error("refresh zookeeper node failed");
                        continue;
                    }
                    result.add(conf);
                }
            }
        }
        catch (Exception e) {
            logger.error("fetch children node failed", e);
            return null;
        }
        return result;
    }

    /**
     * 刷新指定路径的配置
     *
     * @param path zookeeper节点
     * @param conf 配置对象
     */
    public boolean refresh(String path, Config conf) {
        try {
            String data = new String(node.getData(path, false, null), "UTF-8");
            if(!Text.isBlank(data)) {
                JSONObject jObject = JSONObject.convert(data);
                conf.properties.clear();
                for(ILink<String, IJSON> link : jObject) {
                    conf.properties.put(link.getOrigin(), link.getDestination().toString());
                }
            }
        }
        catch (Exception e) {
            logger.error("refresh " + path + " failed", e);
            return false;
        }
        return true;
    }
}
