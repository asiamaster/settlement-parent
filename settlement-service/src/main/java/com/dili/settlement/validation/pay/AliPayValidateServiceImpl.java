package com.dili.settlement.validation.pay;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.validation.PayValidateService;
import org.springframework.stereotype.Service;

/**
 * 支付宝数据验证
 */
@Service
public class AliPayValidateServiceImpl extends PayValidateServiceImpl implements PayValidateService {
    @Override
    public Integer supportWay() {
        return SettleWayEnum.ALI_PAY.getCode();
    }
}
