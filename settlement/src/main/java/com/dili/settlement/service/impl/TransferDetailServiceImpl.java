package com.dili.settlement.service.impl;

import com.dili.settlement.domain.TransferDetail;
import com.dili.settlement.mapper.TransferDetailMapper;
import com.dili.settlement.service.TransferDetailService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2021-05-18 16:15:28.
 */
@Service
public class TransferDetailServiceImpl extends BaseServiceImpl<TransferDetail, Long> implements TransferDetailService {

    public TransferDetailMapper getActualDao() {
        return (TransferDetailMapper)getDao();
    }

    @Transactional
    @Override
    public int deleteBySettleOrderId(Long settleOrderId) {
        return getActualDao().deleteBySettleOrderId(settleOrderId);
    }

    @Override
    public List<TransferDetail> listBySettleOrderId(Long settleOrderId) {
        return getActualDao().listBySettleOrderId(settleOrderId);
    }

    @Override
    public List<TransferDetail> listBySettleOrderIdList(List<Long> settleOrderIdList) {
        return getActualDao().listBySettleOrderIdList(settleOrderIdList);
    }
}