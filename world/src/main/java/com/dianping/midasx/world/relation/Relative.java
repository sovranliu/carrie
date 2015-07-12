package com.dianping.midasx.world.relation;

import com.dianping.midasx.base.logic.ComparisonTool;
import com.dianping.midasx.base.logic.grammar.WordLogicalGrammar;
import com.dianping.midasx.base.type.core.IMapping;
import com.dianping.midasx.utility.config.core.IConfig;
import com.dianping.midasx.world.relation.prepare.FieldPrepare;
import com.dianping.midasx.world.relation.prepare.core.IPrepare;
import com.dianping.midasx.base.logic.BooleanRouteDigraph;

/**
 * 对象联系类
 */
public class Relative extends BooleanRouteDigraph<Object, Relative> {
    /**
     * 比较类型
     */
    public String compareType;
    /**
     * 目标准备对象
     */
    public IPrepare prepareOrigin = null;
    /**
     * 目标准备对象
     */
    public IPrepare prepareTarget = null;


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
     * 路由选择
     *
     * @param value 值
     * @return 输入
     */
    @Override
    public Boolean onRoute(Object value) {
        return false;
    }

    /**
     * 推演
     *
     * @param origin 源对象
     * @return 目标对象的条件
     */
    public Condition deduce(final Object origin) {
        return this.clone(
            new IMapping<Relative, Condition>() {
                @Override
                public Condition get(Relative key) {
                    Condition conditon = new Condition();
                    conditon.prepareOrigin = key.prepareOrigin.copy();
                    conditon.compareType = key.compareType;
                    conditon.target = key.prepareTarget.filter(origin);
                    return conditon;
                }
            }
        );
    }

    /**
     * 构建
     *
     * @param conf 配置对象
     * @return 条件对象
     */
    public static Relative build(IConfig conf) {
        Relative result = new Relative();
        result.setCompareType(conf.get("type"));
        result.prepareOrigin = new FieldPrepare(conf.get("field"));
        result.prepareTarget = new FieldPrepare(conf.get("value"));
        //
        Relative firstSon = null;
        for(IConfig confSon : conf.visits("condition")) {
            Relative condSon = build(confSon);
            if(null == firstSon) {
                firstSon = condSon;
                if(WordLogicalGrammar.GRAMMAR_AND.equalsIgnoreCase(confSon.get("link"))) {
                    result.puts(true, firstSon);
                }
                else if(WordLogicalGrammar.GRAMMAR_OR.equalsIgnoreCase(confSon.get("link"))) {
                    result.puts(false, firstSon);
                }
            }
            else {
                if(WordLogicalGrammar.GRAMMAR_AND.equalsIgnoreCase(confSon.get("link"))) {
                    firstSon.puts(true, condSon);
                }
                else if(WordLogicalGrammar.GRAMMAR_OR.equalsIgnoreCase(confSon.get("link"))) {
                    firstSon.puts(false, condSon);
                }
            }
        }
        return result;
    }
}
