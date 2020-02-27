package com.dili.settlement.validation.pay;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.validation.PayValidateService;
import org.springframework.stereotype.Service;

/**
 * POS
 */
@Service
public class POSPayValidateServiceImpl extends PayValidateServiceImpl implements PayValidateService {
    @Override
    public Integer supportWay() {
        return SettleWayEnum.POS.getCode();
    }
}
