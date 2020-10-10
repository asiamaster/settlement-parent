package com.dili.settlement.settle.pay;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.settle.PayService;
import org.springframework.stereotype.Service;

/**
 * 支付宝数据验证
 */
@Service
public class AliPayServiceImpl extends PayServiceImpl implements PayService {
    @Override
    public Integer supportWay() {
        return SettleWayEnum.ALI_PAY.getCode();
    }
}
