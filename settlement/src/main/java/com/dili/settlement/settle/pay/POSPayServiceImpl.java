package com.dili.settlement.settle.pay;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.TradeResponseDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.enums.TradeChannelEnum;
import com.dili.settlement.settle.PayService;
import org.springframework.stereotype.Service;

/**
 * POS
 */
@Service
public class POSPayServiceImpl extends PayServiceImpl implements PayService {

    @Override
    public Integer getTradeChannel() {
        return TradeChannelEnum.POS.getCode();
    }

    @Override
    public void createAccountSerial(SettleOrderDto settleOrderDto, TradeResponseDto tradeResponse, String tradeId) {
        return;
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.POS.getCode();
    }
}
