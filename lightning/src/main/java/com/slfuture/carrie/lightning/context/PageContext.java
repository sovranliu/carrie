package com.slfuture.carrie.lightning.context;

import com.slfuture.carrie.base.type.Table;
import org.apache.velocity.VelocityContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 页面请求
 */
public class PageContext {
    /**
     * 请求的uri
     */
    public String uri = null;
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
    public VelocityContext context = new VelocityContext();


    /**
     * 在上下文中设值
     *
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value) {
        context.put(key, value);
    }

    /**
     * 构建页面请求
     *
     * @param request 请求
     * @return 页面请求
     */
    public static PageContext build(HttpServletRequest request) {
        PageContext result = new PageContext();
        result.uri = request.getRequestURI().substring(1);
        result.request = request;
        for(int i = 0; i < request.getCookies().length; i++) {
            result.cookie.put(request.getCookies()[i].getName(), request.getCookies()[i].getValue());
        }
        for(Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            if(1 == entry.getValue().length) {
                result.parameters.put(entry.getKey(), entry.getValue()[0]);
            }
            else {
                result.parameters.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return result;
    }
}
