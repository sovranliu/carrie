package com.slfuture.carrie.world.entity.db;

import com.slfuture.carrie.base.text.Text;
import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.base.type.core.*;
import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.utility.db.DBTableInfo;
import com.slfuture.carrie.world.Cluster;
import com.slfuture.carrie.world.entity.remote.RemoteManager;
import com.slfuture.carrie.world.invoker.DBInvoker;

/**
 * 数据表簇类
 */
public class RecordCluster extends Cluster {
    /**
     * 簇名称
     */
    public String name;
    /**
     * 数据表
     */
    public DBTableInfo table = null;
    /**
     * 静态方法映射
     */
    public ITable<String, RecordMethod> methods = new Table<String, RecordMethod>();


    /**
     * 调用方法
     *
     * @param object 对象
     * @param method 方法名称
     * @param parameters 参数列表
     * @return 返回值
     */
    public Object call(RecordObject object, String method, Object...parameters) throws Exception {
        return methods.get(method).call(object, parameters);
    }

    /**
     * 构建记录簇
     *
     * @param conf 配置对象
     * @return 簇
     */
    public static RecordCluster build(IXMLNode conf) {
        RecordCluster result = new RecordCluster();
        result.name = conf.get("name");
        String db = Text.substring(conf.get("table"), null, ".");
        result.table = DBTableInfo.build(conf.get("table"), DBInvoker.instance().executor(db));
        for(IXMLNode node : conf.visits("method")) {
            RecordMethod method = RecordMethod.build(db, node);
            result.methods.put(method.name, method);
        }
        result.manager = RecordManager.build(result, conf.visit("manager"));
        return result;
    }
}
