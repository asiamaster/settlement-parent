package com.dili.settlement.validation.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.validation.SettleValidateService;
import com.dili.ss.exception.BusinessException;

/**
 * 结算验证基础实现类
 */
public abstract class SettleValidateServiceImpl implements SettleValidateService {

    @Override
    public void validSubmitParams(SettleOrderDto settleOrderDto) {
        validParamsSpecial(settleOrderDto);
    }

    @Override
    public void validSettleParams(SettleOrderDto settleOrderDto) {
        if (CollUtil.isEmpty(settleOrderDto.getIdList())) {
            throw new BusinessException("", "ID列表为空");
        }
        if (settleOrderDto.getOperatorId() == null) {
            throw new BusinessException("", "结算员ID为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getOperatorName())) {
            throw new BusinessException("", "结算员为空");
        }
        validParamsSpecial(settleOrderDto);
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        return;
    }

}
