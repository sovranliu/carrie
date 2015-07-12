package com.dianping.midasx.world.relation;

import com.dianping.midasx.base.type.core.IMapping;
import com.dianping.midasx.base.xml.core.IXMLNode;
import com.dianping.midasx.utility.config.core.IConfig;
import com.dianping.midasx.world.Cluster;

import java.text.ParseException;

/**
 * 关系类
 */
public class Relation {
    /**
     * 关系模式
     */
    public final static String MODE_1 = "1";
    public final static String MODE_0_1 = "0-1";
    public final static String MODE_1_N = "1-N";
    public final static String MODE_0_N = "0-N";
    public final static String MODE_N = "N";
    /**
     * 条件类型
     */
    public final static String CONDITION_CATEGORY_PREREQUISITE = "prerequisite";
    public final static String CONDITION_CATEGORY_RELATIVE = "relative";
    public final static String CONDITION_CATEGORY_POSTREQUISITE = "postrequisite";


    /**
     * 关系的名称
     */
    public String name;
    /**
     * 目标簇
     */
    public String cluster;
    /**
     * 模式
     */
    public String mode;
    /**
     * 先决条件
     */
    public Condition prerequisite = null;
    /**
     * 联系条件
     */
    public Relative relative = null;
    /**
     * 后决条件
     */
    public Condition postrequisite = null;


    /**
     * 判断关系是单个结果
     *
     * @return 是否是单个结果
     */
    public boolean isSingle() {
        if(MODE_1.equalsIgnoreCase(mode) || MODE_0_1.equalsIgnoreCase(mode)) {
            return true;
        }
        return false;
    }

    /**
     * 推演
     *
     * @param origin 源对象
     * @return 目标对象的条件
     */
    public Condition deduce(Object origin) {
        Condition result = relative.deduce(origin);
        if(null != postrequisite) {
            result.put(true, postrequisite.clone(new IMapping<Condition, Condition>() {
                @Override
                public Condition get(Condition key) {
                    Condition conditon = new Condition();
                    conditon.prepareOrigin = key.prepareOrigin.copy();
                    conditon.compareType = key.compareType;
                    conditon.target = key.target;
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
     * @return 关系
     */
    public static Relation build(IConfig conf) throws ParseException {
        Relation result = new Relation();
        result.name = conf.get("name");
        result.cluster = conf.get("target");
        result.mode = conf.get("mode");
        for(IConfig node : conf.visits("condition")) {
            if(CONDITION_CATEGORY_PREREQUISITE.equals(node.get("category"))) {
                if(null == result.prerequisite) {
                    result.prerequisite = Condition.build(node);
                }
                else {
                    result.prerequisite.put(true, Condition.build(node));
                }
            }
            else if(CONDITION_CATEGORY_RELATIVE.equals(node.get("category"))) {
                if(null == result.relative) {
                    result.relative = Relative.build(node);
                }
                else {
                    result.relative.put(true, Relative.build(node));
                }
            }
            else if(CONDITION_CATEGORY_POSTREQUISITE.equals(node.get("category"))) {
                if(null == result.postrequisite) {
                    result.postrequisite = Condition.build(node);
                }
                else {
                    result.postrequisite.put(true, Condition.build(node));
                }
            }
        }
        return result;
    }
}
