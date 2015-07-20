package com.dianping.midasx.base.logic;

import com.dianping.midasx.base.logic.core.ICondition;

/**
 * 比较条件 V
 */
public class CompareCondition<T, S extends CompareCondition<T, S>> extends LogicalCondition<T, S> implements ICondition<T> {
    /**
     * 比较类型
     */
    public String compareType = ComparisonTool.COMPARETYPE_EQUAL;
    /**
     * 目标值
     */
    public Object target;


    /**
     * 设置类型
     *
     * @param typeString 类型字符串
     */
    public void setCompareType(String typeString) {
        if("EQUAL".equals(typeString)) {
            compareType = ComparisonTool.COMPARETYPE_EQUAL;
        }
        else if("NOTEQUAL".equals(typeString)) {
            compareType = ComparisonTool.COMPARETYPE_NOTEQUAL;
        }
        else if("GREATERTHAN".equals(typeString)) {
            compareType = ComparisonTool.COMPARETYPE_GREATERTHAN;
        }
        else if("GREATEREQUAL".equals(typeString)) {
            compareType = ComparisonTool.COMPARETYPE_GREATEREQUAL;
        }
        else if("LESSTHAN".equals(typeString)) {
            compareType = ComparisonTool.COMPARETYPE_LESSTHAN;
        }
        else if("LESSEQUAL".equals(typeString)) {
            compareType = ComparisonTool.COMPARETYPE_LESSEQUAL;
        }
        else {
            compareType = typeString;
        }
    }

    /**
     * 检查回调
     *
     * @param value 检查目标
     * @return 目标是否通过检查
     */
    @Override
    public boolean onCheck(T value) {
        return ComparisonTool.compare(compareType, value, target);
    }

    /**
     * 转化为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return compareType + " " + target;
    }
}
