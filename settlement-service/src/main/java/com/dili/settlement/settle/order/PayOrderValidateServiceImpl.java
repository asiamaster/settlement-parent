package com.dili.settlement.settle.order;

import com.dili.settlement.component.SettleDispatchHandler;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.EditEnableEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.settle.OrderValidateService;
import com.dili.settlement.settle.impl.OrderValidateServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 支付结算单验证
 */
@Service
public class PayOrderValidateServiceImpl extends OrderValidateServiceImpl implements OrderValidateService {

    @Resource
    private SettleDispatchHandler settleDispatchHandler;

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getEditEnable() == null || settleOrderDto.getEditEnable().equals(EditEnableEnum.YES.getCode())) {
            return;
        }
        settleDispatchHandler.validPayOrderParams(settleOrderDto);
    }

    @Override
    public Integer supportType() {
        return SettleTypeEnum.PAY.getCode();
    }
}
