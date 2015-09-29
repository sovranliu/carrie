package com.slfuture.carrie.world.relation;

import com.slfuture.carrie.base.type.core.IMapping;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.world.relation.prepare.core.IPrepare;

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
    public final static String CONDITION_CATEGORY_RELATIVE = "link";
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
    public Link link = null;
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
        if(null == link) {
            return null;
        }
        Condition result = link.deduce(origin);
        if(null != postrequisite) {
            result.put(true, postrequisite.clone(new IMapping<Condition, Condition>() {
                @Override
                public Condition get(Condition key) {
                    Condition conditon = new Condition();
                    try {
                        conditon.prepareSelf = (IPrepare) (key.prepareSelf.clone());
                    }
                    catch (CloneNotSupportedException e) {}
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
                if(null == result.link) {
                    result.link = Link.build(node);
                }
                else {
                    result.link.put(true, Link.build(node));
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
