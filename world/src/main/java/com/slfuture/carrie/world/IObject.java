package com.slfuture.carrie.world;

/**
 * 世界对象接口
 *
 * 具备关系，方法，事件特性的对象
 */
public interface IObject {
    /**
     * 未知类型
     */
    public final static int TYPE_UNKNOWN = 0;
    /**
     * 记录类型
     */
    public final static int TYPE_RECORD = 1;
    /**
     * 对象类型
     */
    public final static int TYPE_OBJECT = 2;


    /**
     * 获取对象类型
     *
     * @return 对象类型
     */
    public int type();
}
