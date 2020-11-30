package com.dili.settlement.settle.pay;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.settle.PayService;
import com.dili.settlement.settle.impl.SettleServiceImpl;
import org.springframework.ui.ModelMap;

/**
 * 支付数据验证基础类
 */
public abstract class PayServiceImpl extends SettleServiceImpl implements PayService {

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "pay/special";
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        return;
    }

    @Override
    public void settleParamSpecial(SettleOrder po, SettleOrderDto settleOrderDto) {
        po.setSerialNumber(settleOrderDto.getSerialNumber());
        po.setChargeDate(settleOrderDto.getChargeDate());
    }

    @Override
    public void settleAfter(SettleOrder po, SettleOrderDto settleOrderDto) {
        fundAccountService.add(po.getMarketId(), po.getAppId(), po.getAmount());
    }

    @Override
    public void invalidSpecial(SettleOrder po, SettleOrder reverseOrder) {
        fundAccountService.sub(po.getMarketId(), po.getAppId(), po.getAmount());
    }
}
