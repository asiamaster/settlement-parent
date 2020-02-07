package com.dili.settlement.dao;

import com.dili.settlement.domain.FundAccount;
import com.dili.ss.base.MyMapper;

public interface FundAccountMapper extends MyMapper<FundAccount> {
    /**
     * 修改账户金额
     * @param po
     * @return
     */
    int updateAmount(FundAccount po);
}