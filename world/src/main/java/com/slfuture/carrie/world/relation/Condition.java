package com.slfuture.carrie.world.relation;

import com.slfuture.carrie.base.logic.CompareCondition;
import com.slfuture.carrie.base.logic.core.ILogicalGrammar;
import com.slfuture.carrie.base.logic.grammar.WordLogicalGrammar;
import com.slfuture.carrie.base.text.Text;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.world.relation.prepare.PropertyPrepare;
import com.slfuture.carrie.world.relation.prepare.core.IPrepare;

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

    /**
     * 构建
     *
     * @param sentence 条件语句
     * @param grammar 语法
     * @return 条件对象
     */
    public static Condition build(String sentence, ILogicalGrammar grammar) {
        if(null == sentence) {
            return null;
        }
        // TODO:完善条件语句的解析
        String[] operators = {">", "=", "<", "<=", ">=", "!="};
        int i = Text.indexOf(sentence, operators);
        if(-1 == i) {
            return null;
        }
        boolean sentry = false;
        for(String operator : operators) {
            if(operator.equals(sentence.substring(i + 1, i + 2))) {
                sentry = true;
                break;
            }
        }
        Condition result = new Condition();
        if(sentry) {
            result.target = Text.parse(sentence.substring(i + 2).trim());
            result.compareType = sentence.substring(i, i + 2);
            result.prepareSelf = new PropertyPrepare(sentence.substring(0, i).trim());
        }
        else {
            result.target = Text.parse(sentence.substring(i + 1).trim());
            result.compareType = sentence.substring(i, i + 1);
            result.prepareSelf = new PropertyPrepare(sentence.substring(0, i).trim());
        }
        return result;
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
        if(object.getClass().isAssignableFrom(Condition.class)) {
            Condition other = (Condition) object;
            if(null == prepareSelf) {
                if(null != other.prepareSelf) {
                    return false;
                }
            }
            else {
                if(!prepareSelf.equals(other.prepareSelf)) {
                    return false;
                }
            }
            return super.equals(object);
        }
        return false;
    }

    /**
     * 比较
     *
     * @param object 待比较对象
     * @return 比较结果
     */
    @Override
    public boolean equalsIgnoreTarget(Object object) {
        if(null == object) {
            return false;
        }
        if(Condition.class.isAssignableFrom(object.getClass())) {
            Condition other = (Condition) object;
            if(null == prepareSelf) {
                if(null != other.prepareSelf) {
                    return false;
                }
            }
            else {
                if(!prepareSelf.equals(other.prepareSelf)) {
                    return false;
                }
            }
            return super.equalsIgnoreTarget(object);
        }
        return false;
    }
}
