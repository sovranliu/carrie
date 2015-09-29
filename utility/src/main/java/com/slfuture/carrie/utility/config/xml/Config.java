package com.slfuture.carrie.utility.config.xml;

import com.slfuture.carrie.base.text.Text;

/**
 * XML配置类
 */
public class Config extends com.slfuture.carrie.utility.config.Config {
    /**
     * 值
     */
    public String value = null;


    /**
     * 获取指定键的值
     *
     * @param name 键名称
     * @return 值
     */
    @Override
    public String get(String name) {
        if(Text.isBlank(name)) {
            return value;
        }
        return super.get(name);
    }
}
