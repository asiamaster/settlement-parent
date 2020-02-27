package com.dili.settlement.validation.refund;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.validation.RefundValidateService;
import org.springframework.stereotype.Service;

/**
 * 现金
 */
@Service
public class CashRefundValidateServiceImpl extends RefundValidateServiceImpl implements RefundValidateService {
    @Override
    public Integer supportWay() {
        return SettleWayEnum.CASH.getCode();
    }
}
