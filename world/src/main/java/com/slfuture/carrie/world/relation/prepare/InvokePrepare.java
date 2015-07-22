package com.slfuture.carrie.world.relation.prepare;

import com.slfuture.carrie.world.relation.prepare.core.IPrepare;
import org.apache.log4j.Logger;

/**
 * 方法准备类
 */
public class InvokePrepare implements IPrepare {
    /**
     * 日志对象
     */
    private static Logger logger = Logger.getLogger(InvokePrepare.class);
    /**
     * 属性名称
     */
    public String method;
    /**
     * 参数类型列表
     */
    public Class<?>[] parameterTypes;
    /**
     * 参数类型列表
     */
    public Object[] parameters;


    /**
     * 构造函数
     */
    public InvokePrepare() { }

    /**
     * 构造函数
     *
     * @param method 方法名称
     * @param parameterTypes 参数类型列表
     * @param parameters 参数类型列表
     */
    public InvokePrepare(String method, Class<?>[] parameterTypes, Object[] parameters) {
        this.method = method;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    /**
     * 过滤
     *
     * @param origin 源数据
     * @return 目标数据结构
     */
    @Override
    public Object filter(Object origin) {
        return null;
    }

    /**
     * 克隆
     *
     * @return 克隆对象
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        }
        catch(CloneNotSupportedException ex) {
            return null;
        }
    }

    /**
     * 转化为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        String result = null;
        for(Object parameter : parameters) {
            if(null == result) {
                result = "(" + parameter;
            }
            else {
                result += "," + parameter;
            }
        }
        return result + ")";
    }
}
