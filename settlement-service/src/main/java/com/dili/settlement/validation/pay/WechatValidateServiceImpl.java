package com.dili.settlement.validation.pay;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.validation.PayValidateService;
import org.springframework.stereotype.Service;

/**
 * 微信支付
 */
@Service
public class WechatValidateServiceImpl extends PayValidateServiceImpl implements PayValidateService {
    @Override
    public Integer supportWay() {
        return SettleWayEnum.WECHAT_PAY.getCode();
    }
}
