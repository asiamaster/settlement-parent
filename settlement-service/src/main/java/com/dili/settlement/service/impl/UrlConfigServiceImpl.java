package com.dili.settlement.service.impl;

import com.dili.settlement.dao.UrlConfigMapper;
import com.dili.settlement.domain.UrlConfig;
import com.dili.settlement.service.UrlConfigService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-06 17:33:44.
 */
@Service
public class UrlConfigServiceImpl extends BaseServiceImpl<UrlConfig, Long> implements UrlConfigService {

    public UrlConfigMapper getActualDao() {
        return (UrlConfigMapper)getDao();
    }
}