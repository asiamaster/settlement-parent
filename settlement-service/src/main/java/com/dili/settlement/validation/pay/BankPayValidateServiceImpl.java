package com.dili.settlement.validation.pay;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.validation.PayValidateService;
import org.springframework.stereotype.Service;

/**
 * 银行卡支付
 */
@Service
public class BankPayValidateServiceImpl extends PayValidateServiceImpl implements PayValidateService {
    @Override
    public Integer supportWay() {
        return SettleWayEnum.BANK.getCode();
    }
}
