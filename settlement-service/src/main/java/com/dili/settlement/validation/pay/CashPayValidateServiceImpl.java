package com.dili.settlement.validation.pay;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.validation.PayValidateService;
import org.springframework.stereotype.Service;

/**
 * 现金支付
 */
@Service
public class CashPayValidateServiceImpl extends PayValidateServiceImpl implements PayValidateService {

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        return;
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.CASH.getCode();
    }
}
