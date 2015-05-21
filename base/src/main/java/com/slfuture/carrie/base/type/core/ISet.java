package com.slfuture.carrie.base.type.core;

/**
 * 集合接口
 */
public interface ISet<I> extends ICollection<I>, Cloneable {
    /**
     * 遴选出一个元素
     *
     * @return 任意一个元素
     */
    public I offer();

    /**
     * 增加元素
     *
     * @param item 待添加的元素引用
     * @return 操作执行结果
     */
    public boolean add(I item);

    /**
     * 增加元素
     *
     * @param items 待添加的元素引用合集
     * @return 操作执行结果
     */
    public boolean add(ICollection<I> items);

    /**
     * 删除元素
     *
     * @param item 待删除的元素引用
     * @return 操作执行结果
     */
    public boolean remove(I item);

    /**
     * 删除元素
     *
     * @param items 待删除的元素引用合集
     * @return 操作执行结果
     */
    public boolean remove(ICollection<I> items);

    /**
     * 清理所有元素
     */
    public void clear();

    /**
     * 拷贝
     *
     * @return 拷贝后的对象
     */
    public Object clone() throws CloneNotSupportedException;
}
