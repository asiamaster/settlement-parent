package com.dili.settlement.settle.pay;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.TradeResponseDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.enums.TradeChannelEnum;
import com.dili.settlement.settle.PayService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * 现金支付
 */
@Service
public class CashPayServiceImpl extends PayServiceImpl implements PayService {

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "pay/special_cash";
    }

    @Override
    public Integer getTradeChannel() {
        return TradeChannelEnum.CASH.getCode();
    }

    @Override
    public void createAccountSerial(SettleOrderDto settleOrderDto, TradeResponseDto tradeResponse) {
        return;
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.CASH.getCode();
    }
}
