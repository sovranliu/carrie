package com.slfuture.carrie.lightning.access;

import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * 环境配置上下文监听器
 */
public class EnvironmentListener implements ServletContextListener {
    /**
     * Receives notification that the web application initialization
     * process is starting.
     * <p/>
     * <p>All ServletContextListeners are notified of velocityContext
     * initialization before any filters or servlets in the web
     * application are initialized.
     *
     * @param sce the ServletContextEvent containing the ServletContext
     *            that is being initialized
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("EnvironmentListener.contextInitialized(?) execute...");
        // 获取上下文信息
        String root = sce.getServletContext().getRealPath("/WEB-INF");
        System.out.println("web application root = '" + root + "'");
        String log4jPath = root + File.separator + "classes" + File.separator + "log" + File.separator + "log4j.xml";
        System.out.println("log4j configuration path = '" + log4jPath + "'");
        // 配置初始化
        DOMConfigurator.configure(log4jPath);
        System.out.println("EnvironmentListener.contextInitialized(?) executed");
    }

    /**
     * Receives notification that the ServletContext is about to be
     * shut down.
     * <p/>
     * <p>All servlets and filters will have been destroyed before any
     * ServletContextListeners are notified of velocityContext
     * destruction.
     *
     * @param sce the ServletContextEvent containing the ServletContext
     *            that is being destroyed
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("EnvironmentListener.contextDestroyed(?) execute.");
    }
}
