package com.dili.settlement.handler;

import com.dili.settlement.component.ApplicationContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 用于获取application context 对象
 */
@Component
public class ApplicationContextHandler implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.INSTANCE.setApplicationContext(applicationContext);
    }
}
