package com.dianping.midasx.world.entity.db;

import com.dianping.midasx.base.text.Text;
import com.dianping.midasx.base.type.List;
import com.dianping.midasx.base.type.Set;
import com.dianping.midasx.base.type.core.IList;
import com.dianping.midasx.base.xml.core.IXMLNode;
import com.dianping.midasx.utility.db.StoredProcedureParameters;
import com.dianping.midasx.utility.db.core.IDBExecutor;
import com.dianping.midasx.utility.template.Context;
import com.dianping.midasx.utility.template.VelocityTemplate;
import com.dianping.midasx.utility.template.core.ITemplate;
import com.dianping.midasx.world.invoker.DBInvoker;
import org.apache.log4j.Logger;

/**
 * 数据库方法类
 */
public class RecordMethod {
    /**
     * 输入参数类
     */
    public static class InputParameter {
        /**
         * 入参索引
         */
        public int index;


        /**
         * 构造函数
         */
        public InputParameter() { }

        /**
         * 构造函数
         *
         * @param index 索引值
         */
        public InputParameter(int index) {
            this.index = index;
        }
    }

    /**
     * 上下文参数类
     */
    public static class ContextParameter {
        /**
         * 参数名称
         */
        public String name;


        /**
         * 构造函数
         */
        public ContextParameter() { }

        /**
         * 构造函数
         *
         * @param name 参数名称
         */
        public ContextParameter(String name) {
            this.name = name;
        }
    }

    /**
     * SQL方法类
     */
    public static class SQLMethod {
        /**
         * SQL模板
         */
        public ITemplate template = null;
        /**
         * 参数列表：整型和字符串混合使用
         */
        public IList<Object> parameters = new List<Object>();
    }

    /**
     * 存储过程方法类
     */
    public static class SPMethod {
        /**
         * 存储过程名称
         */
        public String name;
        /**
         * 参数列表：整型和字符串混合使用
         */
        public IList<Object> parameters = new List<Object>();
    }


    /**
     * 参数前缀
     */
    public final static String PARAMETER_PREFIX = "PARAMETER_";

    /**
     * 日志对象
     */
    protected static Logger logger = Logger.getLogger(RecordMethod.class);
    /**
     * 数据库名称
     */
    public String db;
    /**
     * 方法名称
     */
    public String name;
    /**
     * 模式
     */
    public String mode;
    /**
     * 存储过程方法
     */
    public SPMethod spMethod = null;
    /**
     * SQL方法
     */
    public SQLMethod sqlMethod = null;


    /**
     * 判断是否是成员方法
     *
     * @return 是否是成员方法
     */
    public boolean isMember() {
        if(null != spMethod) {
            for(Object object : spMethod.parameters) {
                if(object instanceof InputParameter) {
                    return true;
                }
            }
        }
        else if(null != sqlMethod) {
            for(Object object : sqlMethod.parameters) {
                if(object instanceof InputParameter) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 方法调用
     *
     * @param parameters 参数列表
     * @return 返回值
     */
    public Object call(Object...parameters) throws Exception {
        if(null != sqlMethod) {
            Context context = new Context();
            for(int i = 0; i < parameters.length; i++) {
                context.put(PARAMETER_PREFIX + i, parameters[i]);
            }
            String sql = sqlMethod.template.render(context);
            if(IDBExecutor.MODE_LOAD.equals(mode)) {
                return DBInvoker.instance().load(db, sql);
            }
            else if(IDBExecutor.MODE_SELECT.equals(mode)) {
                return DBInvoker.instance().select(db, sql);
            }
            else if(IDBExecutor.MODE_ALTER.equals(mode)) {
                return DBInvoker.instance().alter(db, sql);
            }
            else if(IDBExecutor.MODE_INSERT.equals(mode)) {
                return DBInvoker.instance().insert(db, sql);
            }
        }
        else if(null != spMethod) {
            StoredProcedureParameters spParameters = new StoredProcedureParameters();
            for(int i = 0; i < spMethod.parameters.size(); i++) {
                if(spMethod.parameters.get(i) instanceof Integer) {
                    spParameters.put(i, spMethod.parameters.get(i));
                }
                else if(spMethod.parameters.get(i) instanceof String) {
                    spParameters.put(i, spMethod.parameters.get(i));
                }
                else if(spMethod.parameters.get(i) instanceof InputParameter) {
                    spParameters.put(i, parameters[((InputParameter)spMethod.parameters.get(i)).index]);
                }
                else {
                    logger.error("invalid sp parameter\nmethod = " + name + ", sp = " + spMethod.name);
                    throw new IllegalArgumentException();
                }
            }
            if(IDBExecutor.MODE_LOAD.equals(mode)) {
                return DBInvoker.instance().load(db, spMethod.name, spParameters);
            }
            else if(IDBExecutor.MODE_SELECT.equals(mode)) {
                return DBInvoker.instance().select(db, spMethod.name, spParameters);
            }
            else if(IDBExecutor.MODE_ALTER.equals(mode)) {
                return DBInvoker.instance().alter(db, spMethod.name, spParameters);
            }
            else {
                logger.error("invalid sp mode\nmethod = " + name + ", sp = " + spMethod.name);
                throw new IllegalArgumentException();
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * 方法调用
     *
     * @param object 执行对象
     * @param parameters 参数列表
     * @return 返回值
     */
    public Object call(RecordObject object, Object...parameters) throws Exception {
        if(null != sqlMethod) {
            Context context = new Context();
            for(Object parameter : sqlMethod.parameters) {
                if(parameter instanceof InputParameter) {
                    context.put(PARAMETER_PREFIX + ((InputParameter) parameter).index, parameters[((InputParameter) parameter).index]);
                }
                else if(parameter instanceof ContextParameter) {
                    context.put(((ContextParameter) parameter).name, object.get(((ContextParameter) parameter).name));
                }
            }
            String sql = sqlMethod.template.render(context);
            if(IDBExecutor.MODE_LOAD.equals(mode)) {
                return DBInvoker.instance().load(db, sql);
            }
            else if(IDBExecutor.MODE_SELECT.equals(mode)) {
                return DBInvoker.instance().select(db, sql);
            }
            else if(IDBExecutor.MODE_ALTER.equals(mode)) {
                return DBInvoker.instance().alter(db, sql);
            }
            else if(IDBExecutor.MODE_INSERT.equals(mode)) {
                return DBInvoker.instance().insert(db, sql);
            }
        }
        else if(null != spMethod) {
            StoredProcedureParameters spParameters = new StoredProcedureParameters();
            for(int i = 0; i < spMethod.parameters.size(); i++) {
                if(spMethod.parameters.get(i) instanceof Integer) {
                    spParameters.put(i, spMethod.parameters.get(i));
                }
                else if(spMethod.parameters.get(i) instanceof String) {
                    spParameters.put(i, spMethod.parameters.get(i));
                }
                else if(spMethod.parameters.get(i) instanceof InputParameter) {
                    spParameters.put(i, parameters[((InputParameter)spMethod.parameters.get(i)).index]);
                }
                else if(spMethod.parameters.get(i) instanceof ContextParameter) {
                    spParameters.put(i, object.get(((ContextParameter)spMethod.parameters.get(i)).name));
                }
                else {
                    logger.error("invalid sp parameter\nmethod = " + name + ", sp = " + spMethod.name);
                    throw new IllegalArgumentException();
                }
            }
            if(IDBExecutor.MODE_LOAD.equals(mode)) {
                return DBInvoker.instance().load(db, spMethod.name, spParameters);
            }
            else if(IDBExecutor.MODE_SELECT.equals(mode)) {
                return DBInvoker.instance().select(db, spMethod.name, spParameters);
            }
            else if(IDBExecutor.MODE_ALTER.equals(mode)) {
                return DBInvoker.instance().alter(db, spMethod.name, spParameters);
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * 构建数据库方法
     *
     * @param db 数据库名称
     * @param conf 数据库配置
     * @return 方法对象
     */
    public static RecordMethod build(String db, IXMLNode conf) {
        RecordMethod result = new RecordMethod();
        result.db = db;
        result.name = conf.get("name");
        result.mode = conf.get("mode");
        String sp = conf.get("sp");
        if(null != sp) {
            result.spMethod = parseSPMethod(sp);
        }
        String sql = conf.get("sql");
        if(null != sql) {
            result.sqlMethod = parseSQLMethod(sql);
        }
        return result;
    }

    /**
     * 解析存储过程方法
     *
     * @param sp 存储过程方法字符串
     * @return 存储过程方法
     */
    private static SPMethod parseSPMethod(String sp) {
        SPMethod result = new SPMethod();
        sp = sp.trim();
        int i = sp.indexOf("(");
        if(-1 == i) {
            return null;
        }
        result.name = sp.substring(0, i).trim();
        int j = sp.lastIndexOf(")");
        if(j < i) {
            return null;
        }
        for(String parameter : sp.substring(i + 1, j).split(",")) {
            parameter = parameter.trim();
            if(parameter.startsWith("'") && parameter.endsWith("'")) {
                result.parameters.add(parameter.substring(1, parameter.length() - 1));
            }
            else if(parameter.startsWith("{") && parameter.endsWith("}")) {
                String string = parameter.substring(1, parameter.length() - 1);
                if(Text.isNumber(string)) {
                    result.parameters.add(new InputParameter(Integer.valueOf(string)));
                }
                else {
                    result.parameters.add(new ContextParameter(string));
                }
            }
            else if(Text.isNumber(parameter)) {
                result.parameters.add(Integer.valueOf(parameter));
            }
            else {
                logger.error("invalid parameter compareType\nsp = " + sp);
                return null;
            }
        }
        return result;
    }

    /**
     * 解析SQL方法
     *
     * @param sql SQL模板
     * @return SQL方法
     */
    private static SQLMethod parseSQLMethod(String sql) {
        SQLMethod result = new SQLMethod();
        result.template = new VelocityTemplate();
        StringBuilder builder = new StringBuilder();
        int i = 0;
        Set<String> history = new Set<String>();
        while(true) {
            int j = sql.indexOf("{", i);
            if(-1 == j) {
                builder.append(sql.substring(i));
                break;
            }
            else {
                builder.append(sql.substring(i, j));
                i = j;
            }
            j = sql.indexOf("}", i);
            if(-1 == j) {
                builder.append(sql.substring(i));
                break;
            }
            String piece = sql.substring(i + 1, j);
            if(!history.contains(piece)) {
                history.add(piece);
                if(Text.isNumber(piece)) {
                    result.parameters.add(new InputParameter(Integer.valueOf(piece)));
                    builder.append("{" + PARAMETER_PREFIX + piece + "}");
                }
                else {
                    result.parameters.add(new ContextParameter(piece));
                    builder.append("{" + piece + "}");
                }
            }
            i = j + 1;
        }
        result.template.setContent(builder.toString());
        return result;
    }
}
