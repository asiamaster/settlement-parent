package com.dili.settlement.settle.pay;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.enums.TradeChannelEnum;
import com.dili.settlement.settle.PayService;
import org.springframework.stereotype.Service;

/**
 * 微信支付
 */
@Service
public class WechatServiceImpl extends PayServiceImpl implements PayService {

    @Override
    public Integer getTradeChannel() {
        return TradeChannelEnum.WECHAT.getCode();
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.WECHAT_PAY.getCode();
    }
}
