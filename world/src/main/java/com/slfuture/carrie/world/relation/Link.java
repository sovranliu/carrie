package com.slfuture.carrie.world.relation;

import com.slfuture.carrie.base.logic.ComparisonTool;
import com.slfuture.carrie.base.logic.grammar.WordLogicalGrammar;
import com.slfuture.carrie.base.type.core.IMapping;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.world.relation.prepare.AgentPrepare;
import com.slfuture.carrie.world.relation.prepare.core.IPrepare;
import com.slfuture.carrie.base.logic.BooleanRouteDigraph;

/**
 * 对象联系类
 */
public class Link extends BooleanRouteDigraph<Object, Link> {
    /**
     * 比较类型
     */
    public String compareType;
    /**
     * 目标准备对象
     */
    public IPrepare prepareSelf = null;
    /**
     * 目标准备对象
     */
    public IPrepare prepareOther = null;


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
            new IMapping<Link, Condition>() {
                @Override
                public Condition get(Link key) {
                    Condition conditon = new Condition();
                    try {
                        conditon.prepareSelf = (IPrepare) (key.prepareSelf.clone());
                    }
                    catch (CloneNotSupportedException e) {}
                    conditon.compareType = key.compareType;
                    conditon.target = key.prepareOther.filter(origin);
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
    public static Link build(IConfig conf) {
        Link result = new Link();
        result.setCompareType(conf.get("type"));
        result.prepareSelf = new AgentPrepare(conf.get("field"));
        result.prepareOther = new AgentPrepare(conf.get("value"));
        //
        Link firstSon = null;
        for(IConfig confSon : conf.visits("condition")) {
            Link condSon = build(confSon);
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
