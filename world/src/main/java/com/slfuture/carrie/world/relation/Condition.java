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
        Condition result = null;
        String[] links = new String[2];
        links[0] = grammar.and();
        links[1] = grammar.or();
        int start = 0;
        int linkType = ILogicalGrammar.GRAMMAR_UNDEFINED;
        while(true) {
            String clause = null;
            int i = Text.indexOf(sentence, links, start);
            if(-1 == i) {
                clause = sentence.substring(start);
                start = -1;
            }
            else {
                clause = sentence.substring(start, i);
                start = sentence.indexOf(" ", i);
            }
            clause = clause.trim();
            //
            String[] operators = {">", "=", "<", "<=", ">=", "!="};
            int j = Text.indexOf(clause, operators);
            if(-1 == j) {
                break;
            }
            boolean sentry = false;
            for(String operator : operators) {
                if(operator.equals(clause.substring(j, j + 2))) {
                    sentry = true;
                    break;
                }
            }
            Condition condition = new Condition();
            if(sentry) {
                condition.target = Text.parse(clause.substring(j + 2).trim());
                condition.compareType = clause.substring(j, j + 2);
                condition.prepareSelf = new PropertyPrepare(clause.substring(0, j).trim());
            }
            else {
                condition.target = Text.parse(clause.substring(j + 1).trim());
                condition.compareType = clause.substring(j, j + 1);
                condition.prepareSelf = new PropertyPrepare(clause.substring(0, j).trim());
            }
            //
            if(null == result) {
                result = condition;
            }
            else {
                if(ILogicalGrammar.GRAMMAR_AND == linkType) {
                    result.put(true, condition);
                }
                else if(ILogicalGrammar.GRAMMAR_OR == linkType) {
                    result.put(false, condition);
                }
            }
            //
            if(-1 == start) {
                break;
            }
            else {
                if(grammar.and().equalsIgnoreCase(sentence.substring(i).substring(0, grammar.and().length()))) {
                    linkType = ILogicalGrammar.GRAMMAR_AND;
                }
                else if(grammar.or().equalsIgnoreCase(sentence.substring(i).substring(0, grammar.or().length()))) {
                    linkType = ILogicalGrammar.GRAMMAR_OR;
                }
            }
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
