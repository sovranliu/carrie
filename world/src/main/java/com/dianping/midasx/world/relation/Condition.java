package com.dianping.midasx.world.relation;

import com.dianping.midasx.base.logic.CompareCondition;
import com.dianping.midasx.world.relation.prepare.FieldPrepare;

/**
 * 对象条件类
 */
public class Condition extends CompareCondition<Object, Condition> {
    /**
     * 目标准备对象
     */
    public FieldPrepare prepare = null;


    /**
     * 检查回调
     *
     * @param target 检查目标
     * @return 目标是否通过检查
     */
    @Override
    public boolean onCheck(Object target) {
        if(null != prepare) {
            target = prepare.filter(target);
        }
        return super.onCheck(target);
    }
}
