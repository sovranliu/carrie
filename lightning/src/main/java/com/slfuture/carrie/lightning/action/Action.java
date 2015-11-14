package com.slfuture.carrie.lightning.action;

import com.slfuture.carrie.base.model.Path;
import com.slfuture.carrie.base.model.core.IModule;
import com.slfuture.carrie.base.type.core.IList;
import com.slfuture.carrie.lightning.Lightning;
import com.slfuture.carrie.lightning.action.core.IAction;
import com.slfuture.carrie.lightning.context.PageContext;
import com.slfuture.carrie.lightning.context.PageVisitor;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.StringWriter;

/**
 * 动作处理器
 */
public abstract class Action implements IModule {
    /**
     * 导入信息
     */
    public class Import {
        /**
         * java动作类
         */
        public Class<?> actionClass = null;
        /**
         * 动作路径
         */
        public String actionUrl = null;


        /**
         * 构造函数
         *
         * @param head 头
         */
        public Import(String head) {
            if(head.endsWith(".java")) {
                try {
                    actionClass = Class.forName(head.substring(0, head.length() - ".java".length()));
                }
                catch (ClassNotFoundException ex) {
                    throw new RuntimeException("head class not found in " + head, ex);
                }
            }
            else if(head.endsWith(".action")) {
                actionUrl = head.substring(0, head.length() - ".action".length());
            }
        }

        /**
         * 调用
         *
         * @param visitor 访问者
         * @param context 上下文
         * @return 下一步动作
         */
        public String invoke(PageVisitor visitor, PageContext context) throws Exception {
            if(null != actionClass) {
                IAction action = (IAction) actionClass.newInstance();
                return action.execute(visitor, context);
            }
            else if(null != actionUrl) {
                return Lightning.actions.get(actionUrl).invoke(visitor, context);
            }
            return null;
        }
    }


    /**
     * 路由Action名称
     */
    public final static String ACTION_ROUTE = "route";

    /**
     * 日志对象
     */
    protected static Logger logger = null;
    /**
     * 脚本文件
     */
    public File file = null;
    /**
     * 访问路径
     */
    public Path uri = null;
    /**
     * 加载前置动作名称
     */
    public IList<Import> imports = null;


    /**
     * 执行
     *
     * @param visitor 访问者
     * @param context 上下文
     * @return 下一步动作
     */
    public abstract String execute(PageVisitor visitor, PageContext context) throws Exception;

    /**
     * 调用
     *
     * @param visitor 访问者
     * @param context 上下文
     * @return 下一步动作
     */
    public String invoke(PageVisitor visitor, PageContext context) throws Exception {
        String result = null;
        if(null != imports) {
            for(Import importObject : imports) {
                result = importObject.invoke(visitor, context);
                if(null != result) {
                    break;
                }
            }
        }
        if(null == result) {
            result = execute(visitor, context);
        }
        return result;
    }

    /**
     * 运行
     *
     * @param visitor 访问者
     * @param context 上下文
     */
    public void run(PageVisitor visitor, PageContext context) throws Exception {
        String result = invoke(visitor, context);
        if(null == result) {
            return;
        }
        if(result.endsWith(".json")) {
            visitor.response.setContentType("application/json");
        }
        if(result.endsWith(".xml")) {
            visitor.response.setContentType("application/xml");
        }
        else if(result.endsWith(".html")) {
            visitor.response.setContentType("text/html");
        }
        else if(result.endsWith(".txt")) {
            visitor.response.setContentType("text/plain");
        }
        else if(result.endsWith(".js")) {
            visitor.response.setContentType("application/x-javascript");
        }
        else if(result.endsWith(".action")) {
            Lightning.doAction(result.substring(0, result.length() - ".action".length()), visitor, context);
            return;
        }
        else if(result.endsWith(".code")) {
            visitor.response.sendError(Integer.valueOf(result.replace(".code", "")));
            return;
        }
        // 渲染指定模板
        Template template = Velocity.getTemplate(result);
        if(null == template) {
            logger.error("template[" + result + "] not exist");
            return;
        }
        if(result.endsWith(".url")) {
            StringWriter writer = new StringWriter();
            template.merge(context.velocityContext, writer);
            writer.close();
            visitor.response.sendRedirect(writer.toString());
        }
        else {
            template.merge(context.velocityContext, visitor.response.getWriter());
        }
    }
}
