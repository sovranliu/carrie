package com.slfuture.carrie.base.logic;

import com.slfuture.carrie.base.logic.core.ICondition;

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

    /**
     * 比较
     *
     * @param object 待比较对象
     * @return 比较结果
     */
    @Override
    public boolean equals(Object object) {
        if(null == object) {
            return false;
        }
        if(CompareCondition.class.isAssignableFrom(object.getClass())) {
            CompareCondition<T, S> other = (CompareCondition<T, S>) object;
            if(!compareType.equals(other.compareType)) {
                return false;
            }
            if(null == target) {
                if(null != other.target) {
                    return false;
                }
            }
            else {
                if(!target.equals(other.target)) {
                    return false;
                }
            }
            CompareCondition pairSelf = null;
            CompareCondition pairOther = null;
            pairSelf = get(true);
            pairOther = other.get(true);
            if((null == pairSelf) ^ (null == pairOther)) {
                return false;
            }
            else if(null != pairSelf && !pairSelf.equals(pairOther) ) {
                return false;
            }
            pairSelf = get(false);
            pairOther = other.get(false);
            if((null == pairSelf) ^ (null == pairOther)) {
                return false;
            }
            else if(null != pairSelf && !pairSelf.equals(pairOther) ) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 比较
     *
     * @param object 待比较对象
     * @return 比较结果
     */
    public boolean equalsIgnoreTarget(Object object) {
        if(null == object) {
            return false;
        }
        if(CompareCondition.class.isAssignableFrom(object.getClass())) {
            CompareCondition<T, S> other = (CompareCondition<T, S>) object;
            if(!compareType.equals(other.compareType)) {
                return false;
            }
            CompareCondition pairSelf = null;
            CompareCondition pairOther = null;
            pairSelf = get(true);
            pairOther = other.get(true);
            if((null == pairSelf) ^ (null == pairOther)) {
                return false;
            }
            else if(null != pairSelf && !pairSelf.equalsIgnoreTarget(pairOther) ) {
                return false;
            }
            pairSelf = get(false);
            pairOther = other.get(false);
            if((null == pairSelf) ^ (null == pairOther)) {
                return false;
            }
            else if(null != pairSelf && !pairSelf.equalsIgnoreTarget(pairOther) ) {
                return false;
            }
            return true;
        }
        return false;
    }
}
