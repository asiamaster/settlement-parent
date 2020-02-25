package com.dili.settlement.service;

import com.dili.settlement.domain.MarketApplication;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-25 10:31:29.
 */
public interface MarketApplicationService extends BaseService<MarketApplication, Long> {
    /**
     * 判断当前应用是否接入
     * @param marketId
     * @param appId
     * @return
     */
    boolean existsApp(Long marketId, Long appId);
}