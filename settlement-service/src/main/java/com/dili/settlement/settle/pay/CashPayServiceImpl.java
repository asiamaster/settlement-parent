package com.dili.settlement.settle.pay;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.settle.PayService;
import org.springframework.stereotype.Service;

/**
 * 现金支付
 */
@Service
public class CashPayServiceImpl extends PayServiceImpl implements PayService {

    @Override
    public Integer supportWay() {
        return SettleWayEnum.CASH.getCode();
    }
}
