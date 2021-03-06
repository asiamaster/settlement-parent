package com.dili.settlement.settle.pay;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.TradeResponseDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.enums.TradeChannelEnum;
import com.dili.settlement.settle.PayService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * 虚拟支付
 */
@Service
public class VirtualPayServiceImpl extends PayServiceImpl implements PayService {

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "pay/special_virtual";
    }

    @Override
    public Integer getTradeChannel() {
        return TradeChannelEnum.VIRTUAL.getCode();
    }

    @Override
    public void createAccountSerial(SettleOrderDto settleOrderDto, TradeResponseDto tradeResponse, String tradeId) {
        return;
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.VIRTUAL_PAY.getCode();
    }
}
