package com.slfuture.carrie.lightning;

import com.slfuture.carrie.lightning.context.PageContext;
import com.slfuture.carrie.base.model.Path;
import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.lightning.context.PageVisitor;
import com.slfuture.carrie.utility.config.Configuration;
import org.apache.log4j.Logger;
import org.apache.velocity.app.Velocity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Stack;

/**
 * 主类
 */
public class Lightning {
    /**
     * 日志对象
     */
    public static Logger logger = Logger.getLogger(Lightning.class);

    /**
     * 动作映射
     */
    public static Table<String, Action> actions = null;


    /**
     * 隐藏构造函数
     */
    private Lightning() { }

    /**
     * 系统初始化
     *
     * @param dictionary WEB-INF目录
     * @return 执行结果
     */
    public static boolean initialize(String dictionary) {
        String template = dictionary + File.separator + "template";
        if(!prepareTemplates(template)) {
            return false;
        }
        String action = dictionary + File.separator + "action";
        if(!prepareAction(action)) {
            return false;
        }
        if(!prepareWorld(dictionary)) {
            return false;
        }
        logger.info("lightning initialize successfully");
        return true;
    }

    /**
     * 系统关闭
     */
    public static void terminate() {
        actions = null;
    }

    /**
     * 执行动作
     *
     * @param path 动作路径
     * @param request 请求
     * @param response 回执
     * @return 是否被拦截
     */
    public static boolean doAction(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        PageContext context = null;
        boolean result = false;
        try {
            context = PageContext.build(request);
            result = doAction(path, PageVisitor.build(request, request.getSession(), response), context);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        finally {
            if(null != context) {
                context.destroy();
            }
        }
        return result;
    }

    /**
     * 执行动作
     *
     * @param path 动作路径
     * @param visitor 访问者
     * @param context 上下文
     * @return 是否被拦截
     */
    public static boolean doAction(String path, PageVisitor visitor, PageContext context) throws ServletException {
        Action action = Lightning.actions.get(path);
        if(null == action) {
            Path redirect = new Path(path, "/");
            while(true) {
                if(null == (redirect = redirect.roll(Path.PATH_PARENT, "/"))) {
                    return false;
                }
                action = Lightning.actions.get(redirect.toString() + "/" + Action.ACTION_ROUTE);
                if(null != action) {
                    break;
                }
            }
        }
        try {
            action.run(visitor, context);
        }
        catch(Exception ex) {
            logger.error("lightning action run failed\naction = " + action.uri.toString(), ex);
            throw new ServletException(ex.toString());
        }
        return true;
    }

    /**
     * 初始化模板系统
     *
     * @param dictionary 模板文件目录
     * @return 执行结果
     */
    private static boolean prepareTemplates(String dictionary) {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "file");
        properties.setProperty("file.resource.loader.description", "Lightning Template");
        properties.setProperty("file.resource.loader.path", dictionary);
        properties.setProperty("file.resource.loader.cache", "true");
        properties.setProperty("input.encoding", "UTF-8");
        properties.setProperty("output.encoding", "UTF-8");
        Velocity.init(properties);
        return true;
    }

    /**
     * 初始化动作处理系统
     *
     * @param dictionary 动作脚本目录
     * @return 执行结果
     */
    private static boolean prepareAction(String dictionary) {
        actions = new Table<String, Action>();
        Stack<File> stack = new Stack<File>();
        stack.push(new File(dictionary));
        while(true) {
            if(0 == stack.size()) {
                break;
            }
            File folder = stack.pop();
            for(File file : folder.listFiles()) {
                String path = file.getAbsolutePath().substring(dictionary.length() + 1);
                if(!"/".equals(file.separator)) {
                    path = path.replace(file.separator, "/");
                }
                if(file.isDirectory()) {
                    stack.push(file);
                    continue;
                }
//                if(file.getName().endsWith(".function")) {
//                    path = path.substring(0, path.length() - ".function".length());
//                    // 初始化Action
//                    if(!Action.initFunction(file)) {
//                        logger.error("function (" + path + ") initialize failed");
//                        throw new IllegalAccessError();
//                    }
//                }
                if(file.getName().endsWith(".action")) {
                    path = path.substring(0, path.length() - ".action".length());
                    // 初始化Action
                    Action action = new Action();
                    action.uri = new Path(path, "/");
                    action.file = file;
                    if(!action.initialize()) {
                        logger.error("Action (" + action.uri + ") initialize failed");
                        throw new IllegalAccessError();
                    }
                    actions.put(path, action);
                }
            }
        }
        return true;
    }

    /**
     * 初始化世界系统
     *
     * @param dictionary 目录
     * @return 执行结果
     */
    private static boolean prepareWorld(String dictionary) {
        String confPath = dictionary + File.separator + "classes" + File.separator + "config" + File.separator + "configuration.xml";
        // 初始化配置系统
        if(Configuration.build(confPath)) {
            logger.info("configuration build successfully");
        }
        else {
            logger.error("configuration build failed");
            return false;
        }
        // 初始化世界系统
        return true;
    }
}
