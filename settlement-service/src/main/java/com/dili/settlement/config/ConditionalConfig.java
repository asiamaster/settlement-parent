package com.dili.settlement.config;

import com.dili.settlement.service.CodeService;
import com.dili.settlement.service.impl.RemoteCodeServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 条件配置类
 */
@Configuration
public class ConditionalConfig {

    @Bean
    @ConditionalOnProperty(value = "settlement.code.generator", havingValue = "remote")
    public CodeService codeService() {
        return new RemoteCodeServiceImpl();
    }
}
