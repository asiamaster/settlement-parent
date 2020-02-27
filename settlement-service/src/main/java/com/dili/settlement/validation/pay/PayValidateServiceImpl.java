package com.dili.settlement.validation.pay;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.validation.PayValidateService;
import com.dili.settlement.validation.impl.SettleValidateServiceImpl;

/**
 * 支付数据验证基础类
 */
public abstract class PayValidateServiceImpl extends SettleValidateServiceImpl implements PayValidateService {

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        //根据PRD暂时屏蔽流水号验证
        /*if (StrUtil.isBlank(settleOrderDto.getSerialNumber())) {
            throw new BusinessException("", "流水号为空");
        }*/
    }
}
