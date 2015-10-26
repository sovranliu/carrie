package com.slfuture.carrie.world.event;

import com.slfuture.carrie.base.type.safe.Table;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.world.IEvent;
import com.slfuture.carrie.world.World;

/**
 * 事件中心
 */
public class EventCenter {
    /**
     * 簇名.事件名 与 事件池映射
     */
    private static Table<String, EventPool> pools = new Table<String, EventPool>();


    /**
     * 隐藏构造函数
     */
    private EventCenter() { }

    /**
     * 抛出事件
     *
     * @param self 自身对象
     * @param event 事件对象
     */
    public static void throwEvent(Object self, IEvent event) {
        String key = World.getClusterName(self) + "." + event.name();
        EventPool pool = pools.get(key);
        if(null == pool) {
            return;
        }
        pool.throwEvent(self, event);
    }

    /**
     * 构建管道
     *
     * @param catcher 捕捉者簇名
     * @param conf 配置对象
     */
    public static void createPipe(String catcher, IConfig conf) {
        String key = conf.getString("target") + "." + conf.getString("event");
        EventPool pool = pools.get(key);
        if(null == pool) {
            pool = new EventPool();
            pools.put(key, pool);
        }
        pool.createPipe(catcher, conf.getString("mode"), conf);
    }
}
