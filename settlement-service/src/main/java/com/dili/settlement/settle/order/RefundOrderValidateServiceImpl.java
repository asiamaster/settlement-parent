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
 * 退款结算单验证实现类
 */
@Service
public class RefundOrderValidateServiceImpl extends OrderValidateServiceImpl implements OrderValidateService {

    @Resource
    private SettleDispatchHandler settleDispatchHandler;

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getEditEnable() == null || settleOrderDto.getEditEnable().equals(EditEnableEnum.YES.getCode())) {
            return;
        }
        settleDispatchHandler.validRefundOrderParams(settleOrderDto);
    }

    @Override
    public Integer supportType() {
        return SettleTypeEnum.REFUND.getCode();
    }
}
