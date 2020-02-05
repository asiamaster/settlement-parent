package com.dili.settlement.config;

import com.dili.settlement.service.CodeService;
import com.dili.settlement.service.impl.LocalCodeServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class DefaultConfig {

    @Bean
    @ConditionalOnMissingBean
    public CodeService codeService() {
        return new LocalCodeServiceImpl();
    }
}
