package com.dili.settlement.validation.order;

import com.dili.settlement.component.SettleValidateDispatchHandler;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.EditEnableEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.validation.OrderValidateService;
import com.dili.settlement.validation.impl.OrderValidateServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 支付结算单验证
 */
@Service
public class PayOrderValidateServiceImpl extends OrderValidateServiceImpl implements OrderValidateService {

    @Resource
    private SettleValidateDispatchHandler settleValidateDispatchHandler;

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getEditEnable() == null || settleOrderDto.getEditEnable().equals(EditEnableEnum.YES.getCode())) {
            return;
        }
        settleValidateDispatchHandler.validPayOrderParams(settleOrderDto);
    }

    @Override
    public Integer supportType() {
        return SettleTypeEnum.PAY.getCode();
    }
}
