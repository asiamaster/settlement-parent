package com.dili.settlement.settle.refund;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.settle.RefundService;
import org.springframework.stereotype.Service;

/**
 * 现金
 */
@Service
public class CashRefundServiceImpl extends RefundServiceImpl implements RefundService {

    @Override
    public Integer supportWay() {
        return SettleWayEnum.CASH.getCode();
    }
}
