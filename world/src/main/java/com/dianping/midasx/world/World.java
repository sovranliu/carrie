package com.dianping.midasx.world;

import com.dianping.midasx.base.model.GlobeIdentity;
import org.apache.log4j.Logger;

/**
 * 世界类
 */
public class World {
    /**
     * 日志对象
     */
    public static Logger logger = Logger.getLogger(World.class);


    /**
     * 隐藏构造函数
     */
    private World() { }

    /**
     * 加载指定全局标志的对象
     *
     * @param id 全局标志
     * @return 被查找的指定对象
     */
    public static Object load(GlobeIdentity<?> id) {
        return null;
    }
}
