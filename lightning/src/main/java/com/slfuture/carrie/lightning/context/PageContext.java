package com.slfuture.carrie.lightning.context;

import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.base.type.core.ILink;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 页面请求
 */
public class PageContext {
    /**
     * 日志对象
     */
    private static Logger logger = Logger.getLogger(PageContext.class);
    /**
     * 请求的uri
     */
    public String path = null;
    /**
     * 请求
     */
    public HttpServletRequest request = null;
    /**
     * Cookie信息
     */
    public Table<String, String> cookie = new Table<String, String>();
    /**
     * 参数
     */
    public Table<String, String> parameters = new Table<String, String>();
    /**
     * 上下文
     */
    private Table<String, Object> context = new Table<String, Object>();
    /**
     * 模板上下文
     */
    public VelocityContext velocityContext = new VelocityContext();


    /**
     * 日志
     *
     * @param log 日志内容
     */
    public void log(Object log) {
        logger.info(log);
    }

    /**
     * 获取上下文中的参数
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return context.get(key);
    }

    /**
     * 在上下文中设值
     *
     * @param key 键
     * @param value 值
     */
    public void put(String key, Object value) {
        context.put(key, value);
    }

    /**
     * 在上下文中设值
     *
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value) {
        velocityContext.put(key, value);
    }

    /**
     * 获取带参数的路径
     *
     * @return 带参数的路径
     */
    public String uri() {
        StringBuilder builder = new StringBuilder();
        builder.append(path);
        boolean sentry = false;
        for(ILink<String, String> link : parameters) {
            if(sentry) {
                builder.append("&");
            }
            else {
                sentry = true;
                builder.append("?");
            }
            builder.append(link.origin());
            builder.append("=");
            builder.append(link.destination());
        }
        return builder.toString();
    }

    /**
     * 构建页面请求
     *
     * @param request 请求
     * @return 页面请求
     */
    public static PageContext build(HttpServletRequest request) {
        PageContext result = new PageContext();
        result.path = request.getRequestURI().substring(1);
        result.request = request;
        if(null != request.getCookies()) {
            for(int i = 0; i < request.getCookies().length; i++) {
                String value = request.getCookies()[i].getValue();
                try {
                    value = java.net.URLDecoder.decode(request.getCookies()[i].getValue(), "UTF-8");
                }
                catch(Exception ex) { }
                value = value.replace("'", "");
                value = value.replace(";", "");
                value = value.replace("--", "");
                value = value.replace("*", "");
                result.cookie.put(request.getCookies()[i].getName(), value);
            }
        }
        for(Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String value = null;
            if(1 == entry.getValue().length) {
                value = entry.getValue()[0];
            }
            else {
                value = entry.getValue().toString();
            }
            value = value.replace("'", "");
            value = value.replace(";", "");
            value = value.replace("--", "");
            value = value.replace("*", "");
            result.parameters.put(entry.getKey(), value);
        }
        return result;
    }
}
