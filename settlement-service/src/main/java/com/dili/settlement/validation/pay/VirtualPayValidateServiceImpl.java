package com.dili.settlement.validation.pay;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.validation.PayValidateService;
import org.springframework.stereotype.Service;

/**
 * 虚拟支付
 */
@Service
public class VirtualPayValidateServiceImpl extends PayValidateServiceImpl implements PayValidateService {

    @Override
    public Integer supportWay() {
        return SettleWayEnum.VIRTUAL_PAY.getCode();
    }
}
