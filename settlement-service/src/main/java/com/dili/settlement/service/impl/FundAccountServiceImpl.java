package com.dili.settlement.service.impl;

import com.dili.settlement.mapper.FundAccountMapper;
import com.dili.settlement.domain.FundAccount;
import com.dili.settlement.service.FundAccountService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-06 17:32:45.
 */
@Service
public class FundAccountServiceImpl extends BaseServiceImpl<FundAccount, Long> implements FundAccountService {

    public FundAccountMapper getActualDao() {
        return (FundAccountMapper)getDao();
    }

    @Transactional
    @Override
    public void add(Long marketId, Long appId, Long amount) {
        if (amount == 0L) {
            return;
        }
        FundAccount po = queryOne(marketId, appId);
        if (po == null) {
            throw new BusinessException("", "市场应用账户不存在");
        }
        po.setAmount(po.getAmount() + amount);
        int i = getActualDao().updateAmount(po);
        if (i != 1) {
            throw new BusinessException("", "账户数据已变更,请稍后重试");
        }
    }

    @Transactional
    @Override
    public void sub(Long marketId, Long appId, Long amount) {
        if (amount == 0L) {
            return;
        }
        FundAccount po = queryOne(marketId, appId);
        if (po == null) {
            throw new BusinessException("", "市场应用账户不存在");
        }
        if (po.getAmount() < amount) {//余额不足
            throw new BusinessException("", "市场应用账户余额不足");
        }
        po.setAmount(po.getAmount() - amount);
        int i = getActualDao().updateAmount(po);
        if (i != 1) {
            throw new BusinessException("", "账户数据已变更,请稍后重试");
        }
    }

    @Override
    public FundAccount queryOne(Long marketId, Long appId) {
        FundAccount query = new FundAccount();
        query.setMarketId(marketId);
        query.setAppId(appId);
        return listByExample(query).stream().findFirst().orElse(null);
    }
}