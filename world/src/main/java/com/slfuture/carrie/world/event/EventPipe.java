package com.slfuture.carrie.world.event;

import com.slfuture.carrie.base.model.Method;
import com.slfuture.carrie.base.type.core.ICollection;
import com.slfuture.carrie.utility.config.core.IConfig;
import com.slfuture.carrie.world.IEvent;
import com.slfuture.carrie.world.World;
import com.slfuture.carrie.world.cluster.Cluster;
import com.slfuture.carrie.world.logic.Agent;
import com.slfuture.carrie.world.relation.Condition;
import com.slfuture.carrie.world.relation.Relation;
import org.apache.log4j.Logger;

/**
 * 事件管道
 */
public class EventPipe {
    /**
     * 日志对象
     */
    public static Logger logger = Logger.getLogger(EventPipe.class);
    /**
     * 事件捕捉者簇名
     */
    public String catcher = null;
    /**
     * 模式
     */
    public String mode = null;
    /**
     * 捕捉者关系
     */
    public Relation relation = null;


    /**
     * 抛出事件
     *
     * @param self 事件抛出者
     * @param event 事件对象
     */
    public void throwEvent(Object self, IEvent event) {
        Condition condition = relation.deduce(self);
        if(null == condition) {
            return;
        }
        if(Relation.MODE_1.equals(mode) || Relation.MODE_0_1.equals(mode)) {
            Agent agent = ((Cluster<?>) World.getCluster(catcher)).findAgent(condition);
            Method method = new Method("onEvent", new Class<?>[] {IEvent.class});
            agent.invoke(method, event);
        }
        else {
            ICollection<Agent> agents = ((Cluster<?>) World.getCluster(catcher)).findAgents(condition);
            Method method = new Method("onEvent", new Class<?>[] {IEvent.class});
            for(Agent agent : agents) {
                agent.invoke(method, event);
            }
        }
    }

    /**
     * 构建消息管道
     *
     * @param catcher 捕捉者簇名
     * @param mode 模式
     * @param conf 配置对象
     */
    public static EventPipe build(String catcher, String mode, IConfig conf) {
        EventPipe result = new EventPipe();
        result.catcher = catcher;
        result.mode = mode;
        try {
            result.relation = Relation.build(conf);
        }
        catch(Exception ex) {
            logger.error("call Relation.build(" + conf + ") failed", ex);
            return null;
        }
        result.relation.cluster = catcher;
        return result;
    }
}
