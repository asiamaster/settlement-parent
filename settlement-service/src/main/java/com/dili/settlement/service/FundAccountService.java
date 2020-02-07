package com.dili.settlement.service;

import com.dili.settlement.domain.FundAccount;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-06 17:32:45.
 */
public interface FundAccountService extends BaseService<FundAccount, Long> {

    /**
     * 增加金额
     * @param marketId
     * @param appId
     * @param amount
     */
    void add(Long marketId, Long appId, Long amount);

    /**
     * 扣减金额
     * @param marketId
     * @param appId
     * @param amount
     */
    void sub(Long marketId, Long appId, Long amount);

    /**
     * 根据市场id、应用id查询账户
     * @param marketId
     * @param appId
     * @return
     */
    FundAccount queryOne(Long marketId, Long appId);
}