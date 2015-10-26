package com.slfuture.carrie.world.event;

import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.world.IEvent;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 事件池
 */
public class EventPool {
    /**
     * 日志对象
     */
    public static Logger logger = Logger.getLogger(EventPool.class);
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
            try {
                pipe.throwEvent(self, event);
            }
            catch (Exception ex) {
                logger.error("call EventPipe.throwEvent(?, ?) failed\nself = " + self + "\nevent =" + event, ex);
            }
        }
    }

    /**
     * 构建管道
     *
     * @param catcher 捕捉者簇名
     * @param mode 模式
     * @param conf 配置对象
     */
    public void createPipe(String catcher, String mode, IConfig conf) {
        EventPipe pipe = EventPipe.build(catcher, mode, conf);
        if(null == pipe) {
            return;
        }
        pipes.add(pipe);
    }
}
