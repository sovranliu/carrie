package com.slfuture.carrie.lightning.action;

import com.slfuture.carrie.base.character.Encoding;
import com.slfuture.carrie.base.etc.Serial;
import com.slfuture.carrie.base.text.Text;
import com.slfuture.carrie.base.type.List;
import com.slfuture.carrie.lightning.context.PageContext;
import com.slfuture.carrie.lightning.context.PageVisitor;
import org.apache.log4j.Logger;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;

/**
 * JavaScript脚本动作
 */
public class JavaScriptAction extends Action {
    /**
     * 脚本引擎对象
     */
    public ScriptEngine engine = null;

    /**
     * 编译后的脚本
     */
    public Invocable script = null;
    /**
     * 脚本函数名称
     */
    public String function = null;


    /**
     * 执行
     *
     * @param visitor 访问者
     * @param context 上下文
     * @return 下一步动作
     */
    @Override
    public String execute(PageVisitor visitor, PageContext context) throws Exception {
        return (String) script.invokeFunction(function, visitor, context);
    }


    /**
     * 初始化
     *
     * @return 是否初始化成功
     */
    @Override
    public boolean initialize() {
        if(null == engine) {
            if(!initEngine()) {
                logger.error("script engine initialize failed");
                return false;
            }
        }
        try {
            // 生成脚本运行时
            engine.eval(initContent(file));
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

    /**
     * 初始脚本内容
     *
     * @param file 脚本
     * @return 脚本内容
     */
    public String initContent(File file) throws Exception {
        String text = Text.loadFile(file.getAbsolutePath(), Encoding.ENCODING_UTF8);
        String rootDictionary = Text.substring(file.getAbsolutePath(), null, "WEB-INF" + File.separator + "action" + File.separator) + "WEB-INF" + File.separator + "action" + File.separator;
        function = "f" + Serial.makeSerialString() + Serial.makeLoopInteger();
        String head = "function " + function + "(visitor, context) {\n";
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while(true) {
            int j = Text.indexOf(text, new String[] {"\r", "\n"}, i);
            if(-1 == j) {
                builder.append(head);
                builder.append(text.substring(i));
                builder.append("\n}");
                break;
            }
            String line = text.substring(i, j);
            if(!line.startsWith("#include")) {
                builder.append(head);
                builder.append(text.substring(i));
                builder.append("\n}");
                break;
            }
            i = j + 1;
            String fileName = Text.substring(line, "<", ">");
            if(fileName.endsWith(".function")) {
                builder.append(Text.loadFile(rootDictionary + fileName, Encoding.ENCODING_UTF8));
            }
            else if(fileName.endsWith(".action") || fileName.endsWith(".java")) {
                if(null == imports) {
                    imports = new List<Import>();
                }
                imports.add(new Import(fileName));
            }
        }
        return builder.toString();
    }

    /**
     * 初始化引擎
     *
     * @return 执行结果
     */
    public boolean initEngine() {
        if(null == engine) {
            synchronized (Action.class) {
                if(null == logger) {
                    logger = Logger.getLogger(Action.class);
                }
                if(null == engine) {
                    engine = new ScriptEngineManager().getEngineByName("javascript");
                    try {
                        engine.put("World", new com.slfuture.carrie.lightning.proxy.WorldProxy());
                        engine.eval("function $(s, c) { return World.$(s, c); }");
                        engine.eval("function $$(s, c) { return World.$$(s, c); }");
                    }
                    catch(Exception ex) {
                        logger.error("Script engine initialize velocityContext failed", ex);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
