package com.dili.settlement.settle.refund;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.TradeResponseDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.enums.TradeChannelEnum;
import com.dili.settlement.settle.RefundService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * 现金
 */
@Service
public class CashRefundServiceImpl extends RefundServiceImpl implements RefundService {

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "refund/special_cash";
    }

    @Override
    public Integer getTradeChannel() {
        return TradeChannelEnum.CASH.getCode();
    }

    @Override
    public void createAccountSerial(SettleOrderDto settleOrderDto, TradeResponseDto tradeResponse, String tradeId) {
        return;
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.CASH.getCode();
    }
}
