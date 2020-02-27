package com.dili.settlement.validation.refund;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.validation.RefundValidateService;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;

/**
 * 银行卡
 */
@Service
public class BankRefundValidateServiceImpl extends RefundValidateServiceImpl implements RefundValidateService {

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        if (StrUtil.isBlank(settleOrderDto.getBankCardHolder())) {
            throw new BusinessException("", "银行卡主为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getAccountNumber())) {
            throw new BusinessException("", "银行卡号为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getBankName())) {
            throw new BusinessException("", "银行名称为空");
        }
        //根据PRD暂时屏蔽流水号验证
        /*if (StrUtil.isBlank(settleOrderDto.getSerialNumber())) {
            throw new BusinessException("", "流水号为空");
        }*/
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.BANK.getCode();
    }
}
