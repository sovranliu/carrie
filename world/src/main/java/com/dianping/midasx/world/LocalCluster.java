package com.dianping.midasx.world;

import com.dianping.midasx.base.logic.grammar.WordLogicalGrammar;
import com.dianping.midasx.base.model.Identity;
import com.dianping.midasx.base.text.Text;
import com.dianping.midasx.base.type.Record;
import com.dianping.midasx.base.type.Table;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.IList;
import com.dianping.midasx.base.type.core.IMapping;
import com.dianping.midasx.base.type.core.ISet;
import com.dianping.midasx.utility.config.core.IConfig;
import com.dianping.midasx.world.invoker.DBInvoker;
import com.dianping.midasx.world.invoker.RemoteInvoker;
import com.dianping.midasx.world.relation.Condition;
import com.dianping.midasx.world.relation.Relation;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.ParseException;

/**
 * 本地簇类
 */
public class LocalCluster extends Cluster {
    /**
     * 构建簇
     *
     * @param conf 配置对象
     * @return 簇
     */
    public static LocalCluster build(IConfig conf) throws ParseException {
        LocalCluster result = new LocalCluster();
        result.idField = conf.get("id");
        result.name = conf.get("name");
        String table = conf.get("table");
        if(null != table) {
            result.db = table.substring(0, table.indexOf("."));
            result.table = table.substring(table.indexOf(".") + 1);
        }
        result.clazz = conf.get("clazz");
        for(IConfig confSon : conf.visits("relations/relation")) {
            result.put(confSon.get("name"), Relation.build(confSon));
        }
        return result;
    }

    /**
     * 加载指定条件的对象
     *
     * @param clazz 对象类型
     * @param condition 查找条件
     * @return 被查找的指定对象
     */
    @Override
    public <T> T find(Class<T> clazz, Condition condition) {
        if(null != this.clazz) {
            try {
                Class<?> targetClazz = Class.forName(this.clazz);
                Method method = targetClazz.getMethod("find", Condition.class);
                Object result = method.invoke(null, condition);
                if(null == result) {
                    return null;
                }
                if(result.getClass().isAssignableFrom(clazz)) {
                    return (T) result;
                }
            }
            catch (Exception ex) {
                try {
                    throw ex;
                }
                catch (Exception e) {
                    logger.error("throw failed", e);
                }
            }

            IMapping<String, Object> properties = (Table<String, Object>) RemoteInvoker.instance().call(name, null, "find", condition);
            if(null == properties) {
                return null;
            }
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new ProxyHandler(this, properties));
        }
        String sql = "SELECT * FROM " + table + " WHERE " + condition.toString(new WordLogicalGrammar());
        Record record = DBInvoker.instance().load(db, sql);
        if(null == record) {
            return null;
        }
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new ProxyHandler(this, record));
    }

    /**
     * 加载指定条件的对象集
     *
     * @param clazz 对象类型
     * @param condition 查找条件
     * @return 被查找的指定对象集
     */
    @Override
    public <T> ICollection<T> finds(Class<T> clazz, Condition condition) {
        if(null != clazz) {
            ISet<IMapping<String, Object>> propertiesSet = (ISet<IMapping<String, Object>>) RemoteInvoker.instance().call(name, null, "find", condition);
            if (null == propertiesSet) {
                return null;
            }
            ISet<T> result = null;
            try {
                result = propertiesSet.getClass().newInstance();
            } catch (Exception e) {
                return null;
            }
            for (IMapping<String, Object> properties : propertiesSet) {
                result.add((T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new ProxyHandler(this, properties)));
            }
            return result;
        }
        String sql = "SELECT * FROM " + table + " WHERE " + condition.toString(new WordLogicalGrammar());
        IList<Record> recordList = DBInvoker.instance().select(db, sql);
        if(null == recordList) {
            return null;
        }
        ISet<T> resultSet = null;
        try {
            resultSet = recordList.getClass().newInstance();
        } catch (Exception e) {
            return null;
        }
        for (Record record : recordList) {
            resultSet.add((T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new ProxyHandler(this, record)));
        }
        return resultSet;
    }

    /**
     * 调用
     *
     * @param id 调用者标志符
     * @param method 调用方法
     * @param args 参数
     * @return 调用结果
     */
    @Override
    public Object invoke(Identity<Object> id, String method, Object[] args) {
        if(!Text.isBlank(clazz)) {
            return RemoteInvoker.instance().call(name, id, method, args);
        }
        SQLMethod sqlMethod = methods.get(method);
        if(null == sqlMethod) {
            throw new NoSuchMethodError(method);
        }
        String sql = DBInvoker.instance().generate(sqlMethod.template, args);
        if(SQLMethod.METHOD_LOAD.equalsIgnoreCase(sqlMethod.type)) {
            return DBInvoker.instance().load(db, sql);
        }
        else if(SQLMethod.METHOD_SELECT.equalsIgnoreCase(sqlMethod.type)) {
            return DBInvoker.instance().select(db, sql);
        }
        else if(SQLMethod.METHOD_ALTER.equalsIgnoreCase(sqlMethod.type)) {
            return DBInvoker.instance().alter(db, sql);
        }
        else if(SQLMethod.METHOD_INSERT.equalsIgnoreCase(sqlMethod.type)) {
            return DBInvoker.instance().insert(db, sql);
        }
        return null;
    }
}
