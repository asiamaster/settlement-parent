package com.dili.settlement.service.impl;

import com.dili.settlement.dao.SettleConfigMapper;
import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.service.SettleConfigService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-07 11:11:14.
 */
@Service
public class SettleConfigServiceImpl extends BaseServiceImpl<SettleConfig, Long> implements SettleConfigService {

    public SettleConfigMapper getActualDao() {
        return (SettleConfigMapper)getDao();
    }
}