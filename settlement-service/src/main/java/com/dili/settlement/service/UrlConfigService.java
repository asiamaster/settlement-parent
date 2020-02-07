package com.dili.settlement.service;

import com.dili.settlement.domain.UrlConfig;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-06 17:33:44.
 */
public interface UrlConfigService extends BaseService<UrlConfig, Long> {

    /**
     * 获取业务类型url
     * @param businessType
     * @param type
     * @return
     */
    String getUrl(Integer businessType, Integer type);
}