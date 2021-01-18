package com.dili.settlement.settle.pay;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.TradeResponseDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.enums.TradeChannelEnum;
import com.dili.settlement.settle.PayService;
import org.springframework.stereotype.Service;

/**
 * 银行卡支付
 */
@Service
public class BankPayServiceImpl extends PayServiceImpl implements PayService {
    @Override
    public Integer getTradeChannel() {
        return TradeChannelEnum.E_BANK.getCode();
    }

    @Override
    public void createAccountSerial(SettleOrderDto settleOrderDto, TradeResponseDto tradeResponse) {
        return;
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.BANK.getCode();
    }
}
