package com.dianping.midasx.base.logic;

import com.dianping.midasx.base.type.Map;
import com.dianping.midasx.base.logic.core.ICondition;
import com.dianping.midasx.base.logic.core.IState;
import com.dianping.midasx.base.type.EdgedDoubleLink;
import com.dianping.midasx.base.type.MappingDigraph;
import com.dianping.midasx.base.type.core.ICollection;
import com.dianping.midasx.base.type.core.ISet;

import java.util.Stack;

/**
 * 逻辑校验类
 *
 * T 对象类型， S 子类
 */
public abstract class LogicalCondition<T, S extends LogicalCondition<T, S>> extends BooleanRouteDigraph<T, S> implements ICondition<T>, IState<Boolean, S> {
    /**
     * 条件双头连接类
     */
    private class LogicalLink extends EdgedDoubleLink<Boolean, LogicalLink> {
        /**
         * 连接上绑定的条件对象
         */
        public LogicalMappingDigraph condition = null;
        /**
         * 语法类型
         */
        public int grammar = 0;


        /**
         * 构造函数
         *
         * @param grammar 语法
         */
        public LogicalLink(int grammar) {
            this.grammar = grammar;
        }

        /**
         * 转化为字符串
         *
         * @return 字符串
         */
        @Override
        public String toString() {
            LogicalLink result = this;
            while(null != result.previous()) {
                result = result.previous();
            }
            StringBuilder builder = new StringBuilder();
            builder.append(result.condition.string);
            while(null != result.next()) {
                builder.append(" ");
                getLinkSymbol(grammar, result.next().edge());
                builder.append(" ");
                if(null == result.next().condition.mode() || result.next().edge() == result.next().condition.mode()) {
                    builder.append(result.next().condition.string);
                }
                else {
                    builder.append("(");
                    builder.append(result.next().condition.string);
                    builder.append(")");
                }
                result = result.next();
            }
            return builder.toString();
        }
    }


    /**
     * 条件带标记有向图
     */
    public class LogicalMappingDigraph extends MappingDigraph<Boolean, LogicalMappingDigraph> {
        /**
         * 条件对象
         */
        public S condition;

        /**
         * 转化的条件字符串
         */
        public String string = null;

        /**
         * 模式， null：单元素，true：与联合，false：或联合
         */
        private Boolean mode = null;
        /**
         * 语法类型
         */
        public int grammar = 0;


        /**
         * 构造函数
         *
         * @param grammar 语法
         */
        public LogicalMappingDigraph(int grammar) {
            this.grammar = grammar;
        }


        /**
         * 获取模式
         *
         * @return 模式
         */
        public Boolean mode() {
            return this.mode;
        }

        /**
         * 设置模式
         *
         * @param mode 模式
         */
        public void setMode(boolean mode) {
            if(null == this.mode) {
                this.mode = mode;
            }
        }

        /**
         * 合并
         *
         * @param mergeType 合并方式
         * @param logicalSignedDigraph 条件对象
         */
        public void merge(boolean mergeType, LogicalMappingDigraph logicalSignedDigraph) {
//            if(null != mode() && mode() != mergeType) {
//                string = "(" + string + ")";
//            }
            if(null == logicalSignedDigraph.mode()) {
                string = string + " " + getLinkSymbol(grammar, mergeType) + " " + logicalSignedDigraph.string;
            }
            else if(logicalSignedDigraph.mode()) {
                string = string + " " + getLinkSymbol(grammar, mergeType);
                if(mergeType) {
                    string = string + " " + logicalSignedDigraph.string;
                }
                else {
                    string = string + " (" + logicalSignedDigraph.string + ")";
                }
            }
            else if(!logicalSignedDigraph.mode()) {
                string = string + " " + getLinkSymbol(grammar, mergeType);
                if(mergeType) {
                    string = string + " (" + logicalSignedDigraph.string + ")";
                }
                else {
                    string = string + " " + logicalSignedDigraph.string;
                }
            }
            setMode(mergeType);
        }
    }


    /**
     * 数学语法：&& ||
     */
    public final static int GRAMMAR_MATH = 0;
    /**
     * 单词语法：AND OR
     */
    public final static int GRAMMAR_WORD = 1;


    /**
     * 检查
     *
     * @param target 检查目标
     * @return 目标是否通过检查
     */
    @Override
    public boolean check(T target) {
        return route(target);
    }

    /**
     * 检查回调
     *
     * @param target 检查目标
     * @return 目标是否通过检查
     */
    public abstract boolean onCheck(T target);

    /**
     * 反转
     *
     * @param value 值
     * @return 输入
     */
    @Override
    public Boolean onRoute(T value) {
        return onCheck(value);
    }

    /**
     * 转化为字符串
     *
     * @param grammar 语法类型
     * @return 字符串
     */
    public String toString(int grammar) {
        LogicalLink logicalLink = new LogicalLink(grammar);
        logicalLink.condition = build((S) this, grammar);
        Stack<LogicalMappingDigraph> stack = new Stack<LogicalMappingDigraph>();
        stack.push(logicalLink.condition);
        while(!stack.isEmpty()) {
            LogicalMappingDigraph logicalSignedDigraph = stack.pop();
            final LogicalMappingDigraph trueCondition = logicalSignedDigraph.get(true);
            final LogicalMappingDigraph falseCondition = logicalSignedDigraph.get(false);
            if(null == trueCondition) {
                if(null == falseCondition) {

                }
                else {
                    LogicalLink next = new LogicalLink(grammar);
                    next.setEdge(false);
                    next.condition = falseCondition;
                    logicalLink.setNext(next);
                }
            }
            else {
                if(null == falseCondition) {
                    LogicalLink next = new LogicalLink(grammar);
                    next.setEdge(true);
                    next.condition = trueCondition;
                    logicalLink.setNext(next);
                }
                else {
                    if(null != falseCondition.findPath(new ICondition<LogicalMappingDigraph>() {
                        @Override
                        public boolean check(LogicalMappingDigraph target) {
                            return trueCondition == target;
                        }
                    })) {
                        LogicalLink next = new LogicalLink(grammar);
                        next.setEdge(false);
                        next.condition = falseCondition;
                        logicalLink.setNext(next);
                    }
                    else if(null != trueCondition.findPath(new ICondition<LogicalMappingDigraph>() {
                        @Override
                        public boolean check(LogicalMappingDigraph target) {
                            return falseCondition == target;
                        }
                    })) {
                        LogicalLink next = new LogicalLink(grammar);
                        next.setEdge(true);
                        next.condition = trueCondition;
                        logicalLink.setNext(next);
                    }
                    else {
                        throw new IllegalStateException();
                    }
                }
            }
            if(null == logicalLink.next()) {
                break;
            }
            if(logicalLink.next().condition.get(!logicalLink.next().edge()) == logicalLink.condition.get(!logicalLink.next().edge())) {
                LogicalLink link = logicalLink.previous();
                boolean sentry = true;
                while(null != link) {
                    if(logicalLink.next().condition == link.condition.get(true)) {
                        sentry = false;
                        break;
                    }
                    else if(logicalLink.next().condition == link.condition.get(false)) {
                        sentry = false;
                        break;
                    }
                    link = link.previous();
                }
                if(sentry) {
                    // 合并
                    logicalLink.condition.merge(logicalLink.next().edge(), logicalLink.next().condition);
                    logicalLink.condition.put(logicalLink.next().edge(), logicalLink.next().condition.get(logicalLink.next().edge()));
                    if(null != logicalLink.previous()) {
                        logicalLink = logicalLink.previous();
                    }
                    logicalLink.setNext(null);
                    stack.push(logicalLink.condition);
                    continue;
                }
            }
            // 未合并
            if(null == logicalLink.next()) {
                break;
            }
            logicalLink = logicalLink.next();
            stack.push(logicalLink.condition);
        }
        return logicalLink.toString();
    }

    /**
     * 获取连接符号
     *
     * @param grammar 语法类型
     * @param linkType 连接类型
     * @return 连接字符串
     */
    private String getLinkSymbol(int grammar, boolean linkType) {
        if(linkType) {
            return grammar == 0 ? "&&":"AND";
        }
        else {
            return grammar == 0 ? "||":"OR";
        }
    }

    /**
     * 构建条件有向图
     *
     * @param condition 条件
     * @param grammar 语法
     * @return 条件有向图
     */
    private LogicalMappingDigraph build(S condition, int grammar) {
        ICollection<S> set = condition.finds(null);
        Map<S, LogicalMappingDigraph> map = new Map<S, LogicalMappingDigraph>();
        for(S item : set) {
            LogicalMappingDigraph signedDigraph = map.get(item);
            if(null == signedDigraph) {
                signedDigraph = new LogicalMappingDigraph(grammar);
                signedDigraph.string = item.toString();
                signedDigraph.condition = item;
                map.put(item, signedDigraph);
            }
            S temp = item.get(true);
            if(null != temp) {
                LogicalMappingDigraph tempSignedDigraph = map.get(temp);
                if(null == tempSignedDigraph) {
                    tempSignedDigraph = new LogicalMappingDigraph(grammar);
                    tempSignedDigraph.string = temp.toString();
                    tempSignedDigraph.condition = temp;
                    map.put(temp, tempSignedDigraph);
                }
                signedDigraph.put(true, tempSignedDigraph);
            }
            temp = item.get(false);
            if(null != temp) {
                LogicalMappingDigraph tempSignedDigraph = map.get(temp);
                if(null == tempSignedDigraph) {
                    tempSignedDigraph = new LogicalMappingDigraph(grammar);
                    tempSignedDigraph.string = temp.toString();
                    tempSignedDigraph.condition = temp;
                    map.put(temp, tempSignedDigraph);
                }
                signedDigraph.put(false, tempSignedDigraph);
            }
        }
        return map.get(condition);
    }
}
