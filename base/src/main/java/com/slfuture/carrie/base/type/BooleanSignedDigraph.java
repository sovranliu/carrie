package com.slfuture.carrie.base.type;

import com.slfuture.carrie.base.logic.core.ICondition;
import com.slfuture.carrie.base.type.core.ISet;

import java.util.Stack;

/**
 * 布尔图
 */
public class BooleanSignedDigraph<T extends BooleanSignedDigraph<T>> extends SignedDigraph<Boolean, T> {
    /**
     * 条件双头连接类
     */
    private class LogicalLink extends EdgedDoubleLink<Boolean, LogicalLink> {
        /**
         * 连接上绑定的条件对象
         */
        public LogicalSignedDigraph condition = null;
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
    public class LogicalSignedDigraph extends SignedDigraph<Boolean, LogicalSignedDigraph> {
        /**
         * 条件对象
         */
        public T condition;

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
        public LogicalSignedDigraph(int grammar) {
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
        public void merge(boolean mergeType, LogicalSignedDigraph logicalSignedDigraph) {
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
     * 设置状态转换
     *
     * @param key 输入信号
     * @param parameter 状态
     * @return 新状态码
     */
    public T putAll(Boolean key, T parameter) {
        ISet<T> states = find(null);
        if(null == super.get(key)) {
            super.put(key, parameter);
        }
        states.remove(parameter);
        if(key) {
            for(T state : states) {
                if(null == state.get(true)) {
                    state.put(true, parameter);
                }
            }
        }
        else {
            for(T state : states) {
                if(null == state.get(false)) {
                    state.put(false, parameter);
                }
            }
        }
        return null;
    }

    /**
     * 转化为字符串
     *
     * @param grammar 语法类型
     * @return 字符串
     */
    public String toString(int grammar) {
        LogicalLink logicalLink = new LogicalLink(grammar);
        logicalLink.condition = build((T) this, grammar);
        Stack<LogicalSignedDigraph> stack = new Stack<LogicalSignedDigraph>();
        stack.push(logicalLink.condition);
        while(!stack.isEmpty()) {
            LogicalSignedDigraph logicalSignedDigraph = stack.pop();
            final LogicalSignedDigraph trueCondition = logicalSignedDigraph.get(true);
            final LogicalSignedDigraph falseCondition = logicalSignedDigraph.get(false);
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
                    if(null != falseCondition.search(new ICondition<LogicalSignedDigraph>() {
                        @Override
                        public boolean check(LogicalSignedDigraph target) {
                            return trueCondition == target;
                        }
                    })) {
                        LogicalLink next = new LogicalLink(grammar);
                        next.setEdge(false);
                        next.condition = falseCondition;
                        logicalLink.setNext(next);
                    }
                    else if(null != trueCondition.search(new ICondition<LogicalSignedDigraph>() {
                        @Override
                        public boolean check(LogicalSignedDigraph target) {
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
    private LogicalSignedDigraph build(T condition, int grammar) {
        ISet<T> set = condition.find(null);
        Map<T, LogicalSignedDigraph> map = new Map<T, LogicalSignedDigraph>();
        for(T item : set) {
            LogicalSignedDigraph signedDigraph = map.get(item);
            if(null == signedDigraph) {
                signedDigraph = new LogicalSignedDigraph(grammar);
                signedDigraph.string = item.toString();
                signedDigraph.condition = item;
                map.put(item, signedDigraph);
            }
            T temp = item.get(true);
            if(null != temp) {
                LogicalSignedDigraph tempSignedDigraph = map.get(temp);
                if(null == tempSignedDigraph) {
                    tempSignedDigraph = new LogicalSignedDigraph(grammar);
                    tempSignedDigraph.string = temp.toString();
                    tempSignedDigraph.condition = temp;
                    map.put(temp, tempSignedDigraph);
                }
                signedDigraph.put(true, tempSignedDigraph);
            }
            temp = item.get(false);
            if(null != temp) {
                LogicalSignedDigraph tempSignedDigraph = map.get(temp);
                if(null == tempSignedDigraph) {
                    tempSignedDigraph = new LogicalSignedDigraph(grammar);
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
