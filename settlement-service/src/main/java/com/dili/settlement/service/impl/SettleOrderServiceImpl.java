package com.dili.settlement.service.impl;

import com.dili.settlement.dao.SettleOrderMapper;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.service.SettleOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-05 16:39:11.
 */
@Service
public class SettleOrderServiceImpl extends BaseServiceImpl<SettleOrder, Long> implements SettleOrderService {

    public SettleOrderMapper getActualDao() {
        return (SettleOrderMapper)getDao();
    }
}