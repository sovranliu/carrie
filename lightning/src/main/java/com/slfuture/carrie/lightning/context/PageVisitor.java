package com.slfuture.carrie.lightning.context;

import com.slfuture.carrie.base.interaction.core.IWritable;
import com.slfuture.carrie.base.type.Table;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;

/**
 * 页面访问者
 */
public class PageVisitor implements IWritable<String> {
    /**
     * 回覆
     */
    public HttpServletResponse response = null;
    /**
     * 会话对象
     */
    public HttpSession session = null;
    /**
     * 上下文
     */
    public Table<String, Object> context = null;
    /**
     * IP地址
     */
    public String ip = null;


    /**
     * 获取指定名称的属性
     *
     * @param name 属性名称
     * @return 属性值
     */
    public Object attribute(String name) {
        return session.getAttribute(name);
    }

    /**
     * 设置指定名称的属性
     *
     * @param name 属性名称
     * @param value 属性值
     */
    public void setAttribute(String name, Object value) {
        session.setAttribute(name, value);
    }

    /**
     * 设置Cookie
     *
     * @param key 键
     * @param value 值
     */
    public void setCookie(String key, String value) {
        setCookie(key, value , 0);
    }

    /**
     * 设置Cookie
     *
     * @param key 键
     * @param value 值
     * @param expiry 周期
     */
    public void setCookie(String key, String value, int expiry) {
        if(null == value) {
            Cookie cookie = new Cookie(key, null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        else {
            try {
                value = URLEncoder.encode(value, "utf-8");
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException("setCookie(" + key + "," + value + ") failed", e);
            }
            Cookie cookie = new Cookie(key, value);
            if(0 == expiry) {
                cookie.setMaxAge(60 * 60 * 24 * 7);
            }
            else {
                cookie.setMaxAge(expiry);
            }
            response.addCookie(cookie);
        }
    }

    /**
     * 写入
     *
     * @param data 数据
     */
    @Override
    public void write(String data) throws Exception {
        response.getWriter().write(data);
    }

    /**
     * 构建页面访问者对象
     *
     * @param request 请求
     * @param session 会话
     * @param response 回覆
     * @return 页面访问者
     */
    public static PageVisitor build(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        PageVisitor result = new PageVisitor();
        result.session = session;
        result.response = response;
        result.context = new Table<String, Object>();
        result.ip = fetchIP(request);
        return result;
    }

    /**
     * 从请求中抽取IP
     *
     * @param request 请求
     * @return 真实IP
     */
    public static String fetchIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                }
                catch (UnknownHostException e) { }
                ipAddress= inet.getHostAddress();
            }
        }
        if(ipAddress != null && ipAddress.length() > 15) {
            if(ipAddress.indexOf(",") > 0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
