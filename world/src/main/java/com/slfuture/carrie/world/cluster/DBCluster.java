package com.slfuture.carrie.world.cluster;

import com.slfuture.carrie.base.logic.grammar.WordLogicalGrammar;
import com.slfuture.carrie.base.model.Method;
import com.slfuture.carrie.base.type.Record;
import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.base.type.core.ILink;
import com.slfuture.carrie.utility.template.Context;
import com.slfuture.carrie.world.invoker.DBInvoker;
import com.slfuture.carrie.world.relation.Condition;

/**
 * 数据库簇类
 */
public class DBCluster extends Cluster<Record> {
    /**
     * SQL方法
     */
    public class SQLMethod {
        /**
         * 方法类型
         */
        public final static String TYPE_LOAD = "LOAD";
        public final static String TYPE_SELECT = "SELECT";
        public final static String TYPE_ALTER = "ALTER";
        public final static String TYPE_INSERT = "INSERT";


        /**
         * 类型
         */
        public String type;
        /**
         * 模版
         */
        public String template;
    }


    /**
     * 数据库名称
     */
    public String dbName;
    /**
     * 表名称
     */
    public String tableName;
    /**
     * 方法映射
     */
    public Table<String, SQLMethod> methods = new Table<String, SQLMethod>();


    /**
     * 查找符合条件的对象
     *
     * @param condition 查找条件
     * @return 查找到的指定对象
     */
    @Override
    public Record find(Condition condition) {
        return DBInvoker.instance().load(dbName, "SELECT * FROM " + tableName + " WHERE " + condition.toString(new WordLogicalGrammar()));
    }

    /**
     * 查找符合条件的对象集
     *
     * @param condition 查找条件
     * @return 查找到的对象集
     */
    @Override
    public ICollection<Record> finds(Condition condition) {
        return DBInvoker.instance().select(dbName, "SELECT * FROM " + tableName + " WHERE " + condition.toString(new WordLogicalGrammar()));
    }

    /**
     * 调用方法
     *
     * @param target 被调用对象
     * @param method 方法
     * @param args 参数列表
     * @return 调用结果
     */
    @Override
    public Object invoke(Record target, Method method, Object...args) {
        SQLMethod sqlMethod = methods.get(method.name);
        if(null == sqlMethod) {
            throw new RuntimeException(new NoSuchMethodException(method.toString()));
        }
        Context context = new Context();
        for(ILink<String, Object> link : target) {
            context.put(link.origin(), link.destination());
        }
        for(int i = 0; i < args.length; i++) {
            context.put(String.valueOf(i), args[i]);
        }
        if(SQLMethod.TYPE_LOAD.equalsIgnoreCase(sqlMethod.type)) {
            return DBInvoker.instance().load(dbName, DBInvoker.instance().generate(sqlMethod.template, context));
        }
        else if(SQLMethod.TYPE_SELECT.equalsIgnoreCase(sqlMethod.type)) {
            return DBInvoker.instance().select(dbName, DBInvoker.instance().generate(sqlMethod.template, context));
        }
        else if(SQLMethod.TYPE_ALTER.equalsIgnoreCase(sqlMethod.type)) {
            return DBInvoker.instance().alter(dbName, DBInvoker.instance().generate(sqlMethod.template, context));
        }
        else if(SQLMethod.TYPE_INSERT.equalsIgnoreCase(sqlMethod.type)) {
            return DBInvoker.instance().insert(dbName, DBInvoker.instance().generate(sqlMethod.template, context));
        }
        throw new RuntimeException("invalid method type : " + sqlMethod.type);
    }
}
