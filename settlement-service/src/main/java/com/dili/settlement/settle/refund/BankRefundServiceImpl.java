package com.dili.settlement.settle.refund;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.settle.RefundService;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;

/**
 * 银行卡
 */
@Service
public class BankRefundServiceImpl extends RefundServiceImpl implements RefundService {

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
    public void settleParamSpecial(SettleOrder po, SettleOrderDto settleOrderDto) {
        po.setAccountNumber(settleOrderDto.getAccountNumber());
        po.setBankName(settleOrderDto.getBankName());
        po.setBankCardHolder(settleOrderDto.getBankCardHolder());
        po.setSerialNumber(settleOrderDto.getSerialNumber());
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.BANK.getCode();
    }
}
