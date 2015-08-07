package com.slfuture.carrie.lightning;

import com.slfuture.carrie.lightning.context.PageContext;
import com.slfuture.carrie.base.character.Encoding;
import com.slfuture.carrie.base.etc.Serial;
import com.slfuture.carrie.base.model.Path;
import com.slfuture.carrie.base.model.core.IModule;
import com.slfuture.carrie.base.text.Text;
import com.slfuture.carrie.lightning.context.PageVisitor;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;

import javax.script.*;
import java.io.File;

/**
 * 动作处理器
 */
public class Action implements IModule {
    /**
     * 路由Action名称
     */
    public final static String ACTION_ROUTE = "route";

    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(Action.class);
    /**
     * 脚本引擎对象
     */
    public static ScriptEngine engine = null;

    /**
     * 编译后的脚本
     */
    public Invocable script = null;
    /**
     * 脚本文件
     */
    public File file = null;
    /**
     * 访问路径
     */
    public Path uri = null;
    /**
     * 脚本函数名称
     */
    public String function = null;


    /**
     * 执行动作
     *
     * @param visitor 访问者
     * @param context 上下文
     */
    public void run(PageVisitor visitor, PageContext context) throws Exception {
        String result = (String) script.invokeFunction(function, visitor, context);
        if(null == result) {
            return;
        }
        if(result.endsWith(".json")) {
            visitor.response.setContentType("application/json");
        }
        else if(result.endsWith(".html")) {
            visitor.response.setContentType("text/html");
        }
        else if(result.endsWith(".js")) {
            visitor.response.setContentType("application/x-javascript");
        }
        else if(result.endsWith(".action")) {
            Lightning.doAction(result.substring(0, result.length() - ".action".length()), visitor, context);
            return;
        }
        // 渲染指定模板
        Template template = Velocity.getTemplate(result);
        if(null == template) {
            logger.error("template[" + result + "] not exist");
            return;
        }
        template.merge(context.context, visitor.response.getWriter());
    }

    /**
     * 初始化
     *
     * @return 是否初始化成功
     */
    @Override
    public boolean initialize() {
        if(null == engine) {
            synchronized (Action.class) {
                if(null == engine) {
                    engine = new ScriptEngineManager().getEngineByName("javascript");
                    try {
                        engine.eval("function $(s, c) { var v = new com.slfuture.carrie.lightning.prox.WorldProxy(); return v.$(s, c); }");
                        engine.eval("function $$(s, c) { var v = new com.slfuture.carrie.lightning.prox.WorldProxy(); return v.$$(s, c); }");
                    }
                    catch(Exception ex) {
                        logger.error("script engine initialize context failed", ex);
                        return false;
                    }
                }
            }
        }
        try {
            // 生成脚本运行时
            StringBuilder builder = new StringBuilder();
            builder.append("function ");
            function = "f" + Serial.makeSerialNumber();
            builder.append(function);
            builder.append("(visitor, context) {\n");
            builder.append(Text.loadFile(file.getAbsolutePath(), Encoding.ENCODING_UTF8));
            builder.append("\n}");
            // script = ((Compilable) engine).compile(builder.toString());
            engine.eval(builder.toString());
            script = (Invocable) engine;
        }
        catch(Exception ex) {
            logger.error("script load or compile failed, path = " + file.getName(), ex);
            return false;
        }
        return true;
    }

    /**
     * 终止
     */
    @Override
    public void terminate() {
        script = null;
        engine = null;
    }
}
