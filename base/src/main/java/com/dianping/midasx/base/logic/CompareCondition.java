package com.dianping.midasx.base.logic;

import com.dianping.midasx.base.logic.core.ICondition;

import java.lang.reflect.Method;

/**
 * 比较条件 V
 */
public class CompareCondition<T, S extends CompareCondition<T, S>> extends LogicalCondition<T, S> implements ICondition<T> {
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
     * 比较类型
     */
    public String compareType;
    /**
     * 目标值
     */
    public Object value;


    /**
     * 设置类型
     *
     * @param typeString 类型字符串
     */
    public void setCompareType(String typeString) {
        if("EQUAL".equals(typeString)) {
            compareType = COMPARETYPE_EQUAL;
        }
        else if("NOTEQUAL".equals(typeString)) {
            compareType = COMPARETYPE_NOTEQUAL;
        }
        else if("GREATERTHAN".equals(typeString)) {
            compareType = COMPARETYPE_GREATERTHAN;
        }
        else if("GREATEREQUAL".equals(typeString)) {
            compareType = COMPARETYPE_GREATEREQUAL;
        }
        else if("LESSTHAN".equals(typeString)) {
            compareType = COMPARETYPE_LESSTHAN;
        }
        else if("LESSEQUAL".equals(typeString)) {
            compareType = COMPARETYPE_LESSEQUAL;
        }
        else {
            compareType = typeString;
        }
    }

    /**
     * 检查回调
     *
     * @param target 检查目标
     * @return 目标是否通过检查
     */
    @Override
    public boolean onCheck(T target) {
        if(COMPARETYPE_EQUAL.equals(compareType)) {
            if(null == value && null == target) {
                return true;
            }
            else if(null != value && value.equals(target)) {
                return true;
            }
        }
        else if(COMPARETYPE_NOTEQUAL.equals(compareType)) {
            if(null == value && null == target) {
                return false;
            }
            else if(null != value && value.equals(target)) {
                return false;
            }
            return true;
        }
        else {
            try {
                Method method = value.getClass().getDeclaredMethod("compareTo", value.getClass());
                Integer result = null;
                if(COMPARETYPE_GREATERTHAN.equals(compareType)) {
                    if(null == value) {
                        return false;
                    }
                    else {
                        result = (Integer) (method.invoke(value, target));
                        if(result < 0) {
                            return true;
                        }
                    }
                }
                else if(COMPARETYPE_GREATEREQUAL.equals(compareType)) {
                    result = (Integer) (method.invoke(value, target));
                    if(result <= 0) {
                        return true;
                    }
                }
                else if(COMPARETYPE_LESSTHAN.equals(compareType)) {
                    result = (Integer) (method.invoke(value, target));
                    if(result > 0) {
                        return true;
                    }
                }
                else if(COMPARETYPE_LESSEQUAL.equals(compareType)) {
                    result = (Integer) (method.invoke(value, target));
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

    /**
     * 转化为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return compareType + " " + value;
    }
}
