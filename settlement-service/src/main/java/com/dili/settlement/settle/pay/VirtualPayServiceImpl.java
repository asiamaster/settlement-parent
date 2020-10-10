package com.dili.settlement.settle.pay;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.settle.PayService;
import org.springframework.stereotype.Service;

/**
 * 虚拟支付
 */
@Service
public class VirtualPayServiceImpl extends PayServiceImpl implements PayService {

    @Override
    public Integer supportWay() {
        return SettleWayEnum.VIRTUAL_PAY.getCode();
    }
}
