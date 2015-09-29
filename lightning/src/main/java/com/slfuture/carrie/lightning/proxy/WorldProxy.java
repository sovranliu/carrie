package com.slfuture.carrie.lightning.proxy;

import com.slfuture.carrie.base.logic.grammar.SymbolLogicalGrammar;
import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.world.IObject;
import com.slfuture.carrie.world.World;
import com.slfuture.carrie.world.relation.Condition;

/**
 * 世界对象代理类
 */
public class WorldProxy {
    /**
     * 查找单个对象
     *
     * @param cluster 簇名称
     * @param condition 查找条件
     * @return 符合条件的单个对象
     */
    public ObjectProxy $(String cluster, String condition) {
        IObject object = World.<IObject>get(cluster, Condition.build(condition, new SymbolLogicalGrammar()), IObject.class);
        if(null == object) {
            return null;
        }
        return new ObjectProxy(object);
    }

    /**
     * 查找对象集
     *
     * @param cluster 簇名称
     * @param condition 查找条件
     * @return 符合条件的对象集
     */
    public ObjectProxy[] $$(String cluster, String condition) {
        ICollection<IObject> set = World.<IObject>gets(cluster, Condition.build(condition, new SymbolLogicalGrammar()), IObject.class);
        ObjectProxy[] result = new ObjectProxy[set.size()];
        int i = 0;
        for(IObject object : set) {
            result[i++] = new ObjectProxy(object);
        }
        return result;
    }
}
