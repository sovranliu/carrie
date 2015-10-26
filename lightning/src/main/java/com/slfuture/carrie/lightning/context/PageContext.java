package com.slfuture.carrie.lightning.context;

import com.slfuture.carrie.base.etc.Serial;
import com.slfuture.carrie.base.type.Table;
import com.slfuture.carrie.base.type.core.ILink;
import com.slfuture.carrie.utility.config.Configuration;
import com.slfuture.carrie.utility.config.core.IConfig;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
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
    public Table<String, Object> parameters = new Table<String, Object>();
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
        for(ILink<String, Object> link : parameters) {
            if(sentry) {
                builder.append("&");
            }
            else {
                sentry = true;
                builder.append("?");
            }
            if(link.destination() instanceof File) {

            }
            else {
                builder.append(link.origin());
                builder.append("=");
                builder.append(link.destination());
            }
        }
        return builder.toString();
    }

    /**
     * 销毁
     */
    public void destroy() {
        for(ILink<String, Object> link : parameters) {
            if(link.destination() instanceof  File) {
                ((File) link.destination()).deleteOnExit();
            }
        }
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
        if(null == request.getContentType()) {
            return result;
        }
        else if(request.getContentType().startsWith("text/")) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                result.parameters.put("", builder.toString());
            }
            catch (Exception ex) {
                logger.error("fetch plain text failed", ex);
            }
        }
        else if(request.getContentType().contains("multipart")) {
            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(1024 * 5);
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setHeaderEncoding("UTF-8");
                IConfig conf = Configuration.root().visit("/system/upload/size");
                if(null == conf) {
                    upload.setFileSizeMax(1024 * 1024 * 2);
                }
                else {
                    upload.setFileSizeMax(Long.valueOf(conf.get(null)));
                }
                String directory = "/app/data/upload/";
                conf = Configuration.root().visit("/system/upload/directory");
                if(null != conf) {
                    directory = conf.get(null);
                }
                File dir = new File(directory);
                if(!dir.exists()) {
                    dir.mkdirs();
                }
                List<FileItem> fileList = upload.parseRequest(request);
                for(FileItem fileItem : fileList) {
                    if(null != fileItem.getName()) {
                        File file = new File(directory + Serial.makeLoopLong() + "." + fileItem.getName());
                        fileItem.write(file);
                        result.parameters.put(fileItem.getFieldName(), file);
                    }
                    else if(null != fileItem.getString("UTF-8")) {
                        result.parameters.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
                    }
                }
            }
            catch(Exception ex) {
                logger.error("fetch file from request failed", ex);
            }
        }
        return result;
    }
}
