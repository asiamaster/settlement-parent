package com.dili.settlement.component;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.ApplicationContext;

/**
 * application context 持有者
 */
public enum ApplicationContextHolder {

    INSTANCE;

    private ApplicationContext applicationContext;

    /**
     * 赋值
     * @param applicationContext
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取bean对象
     * @param name
     * @return
     */
    public Object getBean(String name) {
        return StrUtil.isNotBlank(name) ? this.applicationContext.getBean(name) : null;
    }
}
