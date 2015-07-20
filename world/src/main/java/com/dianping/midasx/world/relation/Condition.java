package com.dianping.midasx.world.relation;

import com.dianping.midasx.base.logic.CompareCondition;
import com.dianping.midasx.base.logic.grammar.WordLogicalGrammar;
import com.dianping.midasx.base.text.Text;
import com.dianping.midasx.utility.config.core.IConfig;
import com.dianping.midasx.world.relation.prepare.PropertyPrepare;
import com.dianping.midasx.world.relation.prepare.core.IPrepare;

import java.io.Serializable;

/**
 * 对象条件类
 */
public class Condition extends CompareCondition<Object, Condition> implements Serializable {
    /**
     * 目标准备对象
     */
    public IPrepare prepareSelf = null;


    /**
     * 检查回调
     *
     * @param value 检查目标
     * @return 目标是否通过检查
     */
    @Override
    public boolean onCheck(Object value) {
        if(null != prepareSelf) {
            return super.onCheck(prepareSelf.filter(value));
        }
        else {
            return super.onCheck(value);
        }
    }

    /**
     * 转化为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        if(null == prepareSelf) {
            return super.toString();
        }
        return prepareSelf.toString() + " " + super.toString();
    }

    /**
     * 构建
     *
     * @param conf 配置对象
     * @return 条件对象
     */
    public static Condition build(IConfig conf) {
        Condition result = new Condition();
        result.target = Text.parse(conf.get("value"));
        result.setCompareType(conf.get("type"));
        result.prepareSelf = new PropertyPrepare(conf.get("field"));
        //
        Condition firstSon = null;
        for(IConfig confSon : conf.visits("condition")) {
            Condition condSon = build(confSon);
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
