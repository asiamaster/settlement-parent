package com.dili.settlement.service.impl;

import com.dili.settlement.service.CodeService;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 调用远程服务生成
 */
public class RemoteCodeServiceImpl implements CodeService {

    @Resource
    private RestTemplate restTemplate;


    @Override
    public String generate() {
        return null;
    }

    @Override
    public String generate(String prefix) {
        return null;
    }
}
