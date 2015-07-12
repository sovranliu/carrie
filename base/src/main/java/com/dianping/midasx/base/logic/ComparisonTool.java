package com.dianping.midasx.base.logic;

import java.lang.reflect.Method;

/**
 * 比较工具类
 */
public class ComparisonTool {
    /**
     * 未定义
     */
    public final static String COMPARETYPE_UNDEFINED = "";
    /**
     * 等于
     */
    public final static String COMPARETYPE_EQUAL = "=";
    /**
     * 等于
     */
    public final static String COMPARETYPE_NOTEQUAL = "!=";
    /**
     * 大于
     */
    public final static String COMPARETYPE_GREATERTHAN = ">";
    /**
     * 大于等于
     */
    public final static String COMPARETYPE_GREATEREQUAL = ">=";
    /**
     * 小于
     */
    public final static String COMPARETYPE_LESSTHAN = "<";
    /**
     * 小于等于
     */
    public final static String COMPARETYPE_LESSEQUAL = "<=";


    /**
     * 隐藏构造函数
     */
    private ComparisonTool() {}

    /**
     * 比较
     *
     * @param compareType 比较类型
     * @param origin 被比较值
     * @param target 比较值
     * @return 比较结果
     */
    public static boolean compare(String compareType, Object origin, Object target) {
        if(COMPARETYPE_EQUAL.equals(compareType)) {
            if(null == target && null == origin) {
                return true;
            }
            else if(null != target && target.equals(origin)) {
                return true;
            }
        }
        else if(COMPARETYPE_NOTEQUAL.equals(compareType)) {
            if(null == target && null == origin) {
                return false;
            }
            else if(null != target && target.equals(origin)) {
                return false;
            }
            return true;
        }
        else {
            try {
                Method method = target.getClass().getDeclaredMethod("compareTo", target.getClass());
                Integer result = null;
                if(COMPARETYPE_GREATERTHAN.equals(compareType)) {
                    if(null == target) {
                        return false;
                    }
                    else {
                        result = (Integer) (method.invoke(target, origin));
                        if(result < 0) {
                            return true;
                        }
                    }
                }
                else if(COMPARETYPE_GREATEREQUAL.equals(compareType)) {
                    result = (Integer) (method.invoke(target, origin));
                    if(result <= 0) {
                        return true;
                    }
                }
                else if(COMPARETYPE_LESSTHAN.equals(compareType)) {
                    result = (Integer) (method.invoke(target, origin));
                    if(result > 0) {
                        return true;
                    }
                }
                else if(COMPARETYPE_LESSEQUAL.equals(compareType)) {
                    result = (Integer) (method.invoke(target, origin));
                    if(result >= 0) {
                        return true;
                    }
                }
            }
            catch(Exception ex) {
                try {
                    throw ex;
                }
                catch (Exception e) { }
            }
        }
        return false;
    }
}
