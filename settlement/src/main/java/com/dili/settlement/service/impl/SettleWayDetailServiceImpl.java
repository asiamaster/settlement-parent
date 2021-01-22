package com.dili.settlement.service.impl;

import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.mapper.SettleWayDetailMapper;
import com.dili.settlement.service.SettleWayDetailService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-01 14:13:21.
 */
@Service
public class SettleWayDetailServiceImpl extends BaseServiceImpl<SettleWayDetail, Long> implements SettleWayDetailService {

    public SettleWayDetailMapper getActualDao() {
        return (SettleWayDetailMapper)getDao();
    }

    @Override
    public List<SettleWayDetail> listBySettleOrderId(Long settleOrderId) {
        return getActualDao().listBySettleOrderId(settleOrderId);
    }

    @Override
    public List<SettleWayDetail> listBySettleOrderCode(String settleOrderCode) {
        return getActualDao().listBySettleOrderCode(settleOrderCode);
    }

    @Transactional
    @Override
    public int batchUpdateChargeDate(List<SettleWayDetail> settleWayDetailList) {
        return getActualDao().batchUpdateChargeDate(settleWayDetailList);
    }
}