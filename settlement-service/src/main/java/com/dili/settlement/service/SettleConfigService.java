package com.dili.settlement.service;

import com.dili.settlement.domain.SettleConfig;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-07 11:11:14.
 */
public interface SettleConfigService extends BaseService<SettleConfig, Long> {
    /**
     * 根据市场id 分组编码获取唯一加密秘钥(注意数据库数据配置)
     * @param marketId
     * @param groupCode
     * @return
     */
    String getSignKey(Long marketId, int groupCode);
}