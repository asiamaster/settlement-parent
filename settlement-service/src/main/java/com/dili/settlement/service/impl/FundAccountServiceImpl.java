package com.dili.settlement.service.impl;

import com.dili.settlement.dao.FundAccountMapper;
import com.dili.settlement.domain.FundAccount;
import com.dili.settlement.service.FundAccountService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-06 17:32:45.
 */
@Service
public class FundAccountServiceImpl extends BaseServiceImpl<FundAccount, Long> implements FundAccountService {

    public FundAccountMapper getActualDao() {
        return (FundAccountMapper)getDao();
    }
}