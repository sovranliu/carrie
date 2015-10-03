package com.slfuture.carrie.world.event;

import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.world.IEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 事件池
 */
public class EventPool {
    /**
     * 事件管道集合
     */
    private ConcurrentLinkedQueue<EventPipe> pipes = new ConcurrentLinkedQueue<EventPipe>();


    /**
     * 抛出事件
     *
     * @param self 自身对象
     * @param event 事件对象
     */
    public void throwEvent(Object self, IEvent event) {
        for(EventPipe pipe : pipes) {
            pipe.throwEvent(self, event);
        }
    }

    /**
     * 构建管道
     *
     * @param catcher 捕捉者簇名
     * @param conf 配置对象
     */
    public void createPipe(String catcher, IConfig conf) {
        EventPipe pipe = EventPipe.build(catcher, conf);
        if(null == pipe) {
            return;
        }
        pipes.add(pipe);
    }
}
