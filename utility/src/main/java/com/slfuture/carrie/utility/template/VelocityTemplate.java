package com.slfuture.carrie.utility.template;

import com.slfuture.carrie.base.type.core.ILink;
import com.slfuture.carrie.utility.template.core.ITemplate;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;

/**
 * Velocity模板类
 */
public class VelocityTemplate implements ITemplate {
    /**
     * 模板内容
     */
    private String content;


    /**
     * 获取模板内容
     *
     * @return 模板内容
     */
    @Override
    public String content() {
        return content;
    }

    /**
     * 设置模板内容
     *
     * @param content 模板内容
     */
    @Override
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 渲染
     *
     * @param context 上下文
     * @return 渲染后的文本
     */
    @Override
    public String render(Context context) throws Exception {
        VelocityContext velocityContext = new VelocityContext();
        for(ILink<String, Object> link : context) {
            velocityContext.put(link.getOrigin(), link.getDestination());
        }
        StringWriter writer = new StringWriter();
        VelocityEngine engine = new VelocityEngine();
        engine.evaluate(velocityContext, writer, null, content);
        return writer.toString();
    }
}
