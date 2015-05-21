package com.slfuture.carrie.world;

import com.slfuture.carrie.base.logic.core.ICondition;
import com.slfuture.carrie.base.type.Link;
import com.slfuture.carrie.base.type.core.IMapping;
import com.slfuture.carrie.base.xml.core.IXMLNode;
import com.slfuture.carrie.world.condition.FieldCondition;
import com.slfuture.carrie.world.condition.RelatedCondition;

import java.text.ParseException;

/**
 * 关系类
 */
public class Relation extends Link<Cluster, Cluster> {
    /**
     * 关系的名称
     */
    public String name;
    /**
     * 先决条件
     */
    public FieldCondition prerequisite = null;
    /**
     * 联系条件
     */
    public RelatedCondition relative = null;
    /**
     * 后决条件
     */
    public FieldCondition postrequisite = null;


    /**
     * 推演
     *
     * @param origin 源对象
     * @return 目标对象的条件
     */
    public ICondition<IObject> deduce(IObject origin) {
        FieldCondition result = relative.deduce(origin);
        if(null != postrequisite) {
            result.put(true, postrequisite.clone(new IMapping<FieldCondition, FieldCondition>() {
                @Override
                public FieldCondition get(FieldCondition key) {
                    FieldCondition conditon = new FieldCondition();
                    conditon.compareType = key.compareType;
                    conditon.value = key.value;
                    return conditon;
                }
            }));
        }
        return result;
    }

    /**
     * 构建关系
     *
     * @param conf 配置对象
     * @return 簇
     */
    public static Relation build(IXMLNode conf) throws ParseException {
        Relation result = new Relation();
        for(IXMLNode node : conf.visits("prerequisite")) {
            if(null == result.prerequisite) {
                result.prerequisite = FieldCondition.build(node);
            }
            else {
                result.prerequisite.put(true, FieldCondition.build(node));
            }
        }
        for(IXMLNode node : conf.visits("relative")) {
            if(null == result.relative) {
                result.relative = RelatedCondition.build(node);
            }
            else {
                result.relative.put(true, RelatedCondition.build(node));
            }
        }
        for(IXMLNode node : conf.visits("postrequisite")) {
            if(null == result.postrequisite) {
                result.postrequisite = FieldCondition.build(node);
            }
            else {
                result.postrequisite.put(true, FieldCondition.build(node));
            }
        }
        return result;
    }
}
