package com.dili.settlement.service.impl;

import com.dili.settlement.domain.SettleOrderLink;
import com.dili.settlement.mapper.SettleOrderLinkMapper;
import com.dili.settlement.service.SettleOrderLinkService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-11-26 16:29:48.
 */
@Service
public class SettleOrderLinkServiceImpl extends BaseServiceImpl<SettleOrderLink, Long> implements SettleOrderLinkService {

    public SettleOrderLinkMapper getActualDao() {
        return (SettleOrderLinkMapper)getDao();
    }

    @Override
    public String getUrl(Long settleOrderId, int type) {
        SettleOrderLink query = new SettleOrderLink();
        query.setSettleOrderId(settleOrderId);
        query.setType(type);
        SettleOrderLink settleOrderLink = listByExample(query).stream().findFirst().orElse(null);
        return settleOrderLink != null ? settleOrderLink.getUrl() : "";
    }

    @Transactional
    @Override
    public int deleteBySettleOrderId(Long settleOrderId) {
        return getActualDao().deleteBySettleOrderId(settleOrderId);
    }
}