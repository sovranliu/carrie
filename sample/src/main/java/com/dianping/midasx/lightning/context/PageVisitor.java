package com.dianping.midasx.lightning.context;

import com.slfuture.carrie.base.interaction.core.IWritable;
import com.slfuture.carrie.base.type.Table;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
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
     * @param session 会话
     * @param response 回覆
     * @return 页面访问者
     */
    public static PageVisitor build(HttpSession session, HttpServletResponse response) {
        PageVisitor result = new PageVisitor();
        result.session = session;
        result.response = response;
        result.context = new Table<String, Object>();
        return result;
    }
}
