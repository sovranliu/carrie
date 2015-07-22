package com.slfuture.carrie.base.type;

import com.slfuture.carrie.base.logic.core.ICondition;
import com.slfuture.carrie.base.type.core.*;

import java.util.Stack;

/**
 * 带标记有向图类
 */
public class MappingDigraph<K, T extends IMappingDigraph<K, T>> extends Table<K, T> implements IMappingDigraph<K, T> {
    /**
     * 按条件搜索路径
     *
     * @param condition 条件对象
     * @return 查找路径，搜索不到返回null
     */
    @Override
    public IList<T> searchPath(ICondition<T> condition) {
        Stack<T> stack = new Stack<T>();
        stack.push((T) this);
        // 反向指向映射
        Map<T, T> inverseMap = new Map<T, T>();
        while(!stack.isEmpty()) {
            T digraph = stack.pop();
            if(null == condition || condition.check(digraph)) {
                List<T> result = new List<T>();
                while(true) {
                    result.insert(0, digraph);
                    digraph = inverseMap.get(digraph);
                    if(null == digraph) {
                        return result;
                    }
                }
            }
            for(ILink<K, T> link : digraph) {
                if(null == inverseMap.get(link.destination())) {
                    stack.push(link.destination());
                    inverseMap.put(link.destination(), digraph);
                }
            }
        }
        return null;
    }

    /**
     * 按条件查找全部
     *
     * @param condition 条件对象
     * @return 查找结果，搜索不到返回空集合
     */
    @Override
    public ICollection<T> finds(ICondition<T> condition) {
        ISet<T> reached = new Set<T>();
        ISet<T> result = new Set<T>();
        Stack<T> stack = new Stack<T>();
        stack.push((T) this);
        while(!stack.isEmpty()) {
            T digraph = stack.pop();
            reached.add(digraph);
            if(null == condition || condition.check(digraph)) {
                result.add(digraph);
            }
            for(ILink<K, T> link : digraph) {
                if(!reached.contains(link.destination())) {
                    stack.push(link.destination());
                }
            }
        }
        return result;
    }

    /**
     * 按条件查找一个
     *
     * @param condition 条件对象
     * @return 查找结果，搜索不到返回null
     */
    @Override
    public T find(ICondition<T> condition) {
        ISet<T> reached = new Set<T>();
        Stack<T> stack = new Stack<T>();
        stack.push((T) this);
        while(!stack.isEmpty()) {
            T digraph = stack.pop();
            reached.add(digraph);
            if(null == condition || condition.check(digraph)) {
                return digraph;
            }
            for(ILink<K, T> link : digraph) {
                if(!reached.contains(link.destination())) {
                    stack.push(link.destination());
                }
            }
        }
        return null;
    }

    /**
     * 克隆
     *
     * @param convert 转换函数
     * @return 转换后的新图
     */
    public <X extends MappingDigraph<K, X>> X clone(IMapping<T, X> convert) {
        ICollection<T> set = finds(null);
        Map<T, X> map = new Map<T, X>();
        for(T item : set) {
            X signedDigraph = map.get(item);
            if(null == signedDigraph) {
                signedDigraph = convert.get(item);
                map.put(item, signedDigraph);
            }
            for(ILink<K, T> link : item) {
                T temp = link.destination();
                X tempSignedDigraph = map.get(temp);
                if(null == tempSignedDigraph) {
                    tempSignedDigraph = convert.get(temp);
                    map.put(temp, tempSignedDigraph);
                }
                signedDigraph.put(link.origin(), tempSignedDigraph);
            }
        }
        return map.get((T) this);
    }
}
