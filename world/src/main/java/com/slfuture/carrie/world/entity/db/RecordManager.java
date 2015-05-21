package com.slfuture.carrie.world.entity.db;

import com.slfuture.carrie.base.logic.core.ICondition;
import com.slfuture.carrie.base.type.Record;
import com.slfuture.carrie.base.type.Set;
import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.base.type.core.ISet;
import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.utility.db.DBTableInfo;
import com.slfuture.carrie.world.IManager;
import com.slfuture.carrie.world.IObject;
import com.slfuture.carrie.world.invoker.DBInvoker;

/**
 * 数据库记录管理器类
 */
public class RecordManager extends DBTableInfo implements IManager<RecordObject> {
    /**
     * 函数映射
     */
    private Table<String, RecordMethod> functions = new Table<String, RecordMethod>();
    /**
     * 簇对象
     */
    public RecordCluster cluster = null;


    /**
     * 方法调用
     *
     * @param method     方法名
     * @param parameters 参数列表
     * @return 返回值
     */
    public Object call(String method, Object... parameters) throws Exception {
        return functions.get(method).call(parameters);
    }

    /**
     * 按条件查找一个
     *
     * @param condition 条件对象
     * @return 查找结果，搜索不到返回null
     */
    @Override
    public RecordObject load(ICondition<IObject> condition) {
        RecordObject result = new RecordObject();
        result.record = DBInvoker.instance().load(db, "SELECT * FROM " + table + " WHERE " + condition.toString());
        result.cluster = cluster;
        return result;
    }

    /**
     * 条件查找
     *
     * @param condition 条件对象
     * @return 查找到的对象或对象集
     */
    @Override
    public ISet<RecordObject> find(ICondition<IObject> condition) {
        ISet<RecordObject> result = new Set<RecordObject>();
        for(Record record : DBInvoker.instance().select(db, "SELECT * FROM " + table + " WHERE " + condition.toString())) {
            result.add(new RecordObject(record, cluster));
        }
        return result;
    }

    /**
     * 构建记录管理器
     *
     * @param cluster 簇
     * @param conf 配置对象
     * @return 记录管理器
     */
    public static RecordManager build(RecordCluster cluster, IXMLNode conf) {
        RecordManager result = new RecordManager();
        for(IXMLNode node : conf.visits("method")) {
            RecordMethod method = RecordMethod.build(cluster.table.db, node);
            result.functions.put(method.name, method);
        }
        result.cluster = cluster;
        return result;
    }
}
