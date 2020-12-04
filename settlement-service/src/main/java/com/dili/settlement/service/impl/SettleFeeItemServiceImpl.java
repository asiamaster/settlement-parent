package com.dili.settlement.service.impl;

import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.mapper.SettleFeeItemMapper;
import com.dili.settlement.service.SettleFeeItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-11-26 16:30:28.
 */
@Service
public class SettleFeeItemServiceImpl extends BaseServiceImpl<SettleFeeItem, Long> implements SettleFeeItemService {

    public SettleFeeItemMapper getActualDao() {
        return (SettleFeeItemMapper)getDao();
    }

    @Override
    public List<SettleFeeItem> listBySettleOrderId(Long settleOrderId) {
        return getActualDao().listBySettleOrderId(settleOrderId);
    }

    @Override
    public List<SettleFeeItem> listBySettleOrderIdList(List<Long> settleOrderIdList) {
        return getActualDao().listBySettleOrderIdList(settleOrderIdList);
    }
}