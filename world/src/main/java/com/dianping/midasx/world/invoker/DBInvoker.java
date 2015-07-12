package com.dianping.midasx.world.invoker;

import com.dianping.midasx.base.type.Record;
import com.dianping.midasx.base.type.core.IList;
import com.dianping.midasx.utility.db.DBExecutor;
import com.dianping.midasx.utility.db.SQLExecutor;
import com.dianping.midasx.utility.db.StoredProcedureParameters;

/**
 * 数据库调用
 */
public class DBInvoker {
    /**
     * 单件实例
     */
    private static DBInvoker instance = null;
    /**
     * 执行器
     */
    private SQLExecutor executor = null;


    /**
     * 隐藏构造函数
     */
    private DBInvoker() {}

    /**
     * 获取实例对象
     *
     * @return 实例对象
     */
    public static DBInvoker instance() {
        if(null == instance) {
            synchronized (DBInvoker.class) {
                if(null == instance) {
                    instance = new DBInvoker();
                    instance.executor = new SQLExecutor();
                }
            }
        }
        return instance;
    }

    /**
     * 获取数据库执行器
     *
     * @param db
     */
    public DBExecutor executor(String db) {
        return executor.get(db);
    }

    /**
     * 提取一条数据
     *
     * @param db 数据库名称
     * @param sql 待执行的SQL语句
     * @return 唯一记录
     */
    public Record load(String db, String sql) {
        return executor.get(db).load(sql);
    }

    /**
     * 提取数据
     *
     * @param db 数据库名称
     * @param sql 待执行的SQL语句
     * @return 记录集合
     */
    public IList<Record> select(String db, String sql) {
        return executor.get(db).select(sql);
    }

    /**
     * 变更
     *
     * @param db 数据库名称
     * @param sql SQL语句
     * @return 是否执行成功
     */
    public int alter(String db, String sql) {
        return executor.get(db).alter(sql);
    }

    /**
     * 插入
     *
     * @param db 数据库名称
     * @param sql SQL语句
     * @return 自增主键，未能自增主键返回null
     */
    public Long insert(String db, String sql) {
        return executor.get(db).insert(sql);
    }

    /**
     * 调用存储过程提取单记录
     *
     * @param db 数据库名称
     * @param storedProcedureName 存储过程名称
     * @param parameters 存储过程参数集合，顺序必须和SP钟参数申明顺序保持一致
     * @return 唯一记录
     */
    public Record load(String db, String storedProcedureName, StoredProcedureParameters parameters) {
        return executor.get(db).load(storedProcedureName, parameters);
    }

    /**
     * 调用存储过程提取数据
     *
     * @param db 数据库名称
     * @param storedProcedureName 存储过程名称
     * @param parameters 存储过程参数集合，顺序必须和SP钟参数申明顺序保持一致
     * @return 记录集合
     */
    public IList<Record> select(String db, String storedProcedureName, StoredProcedureParameters parameters) {
        return executor.get(db).select(storedProcedureName, parameters);
    }

    /**
     * 调用存储过程执行变更
     *
     * @param db 数据库名称
     * @param storedProcedureName 存储过程名称
     * @param parameters 存储过程参数集合，顺序必须和SP钟参数申明顺序保持一致
     * @return 是否执行成功
     */
    public int alter(String db, String storedProcedureName, StoredProcedureParameters parameters) {
        return executor.get(db).alter(storedProcedureName, parameters);
    }

    /**
     * 根据模版生成SQL语句
     *
     * @param template 模版语句
     * @param args 参数列表
     * @return 生成的SQL语句
     */
    public String generate(String template, Object... args) {
        return null;
    }
}
