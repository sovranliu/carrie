package com.slfuture.carrie.base.type;

/**
 * 记录集合类
 */
public class RecordSet extends Set<Record> {
    /**
     * 增加元素
     *
     * @param item 待添加的元素引用
     * @return 操作执行结果
     */
    @Override
    public boolean add(Record item) {
        return set.add(item);
    }
}
