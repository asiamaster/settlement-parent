package com.dili.settlement.validation.impl;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.validation.OrderValidateService;
import com.dili.ss.exception.BusinessException;

/**
 * 结算单数据验证基础实现类
 */
public abstract class OrderValidateServiceImpl implements OrderValidateService {

    @Override
    public void validParams(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getMarketId() == null) {
            throw new BusinessException("", "市场ID为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getMarketCode())) {
            throw new BusinessException("", "市场编码为空");
        }
        if (settleOrderDto.getAppId() == null) {
            throw new BusinessException("", "应用ID为空");
        }
        if (settleOrderDto.getBusinessType() == null) {
            throw new BusinessException("", "业务类型为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getBusinessCode())) {
            throw new BusinessException("", "业务单号为空");
        }
        if (settleOrderDto.getCustomerId() == null) {
            throw new BusinessException("", "客户ID为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getCustomerName())) {
            throw new BusinessException("", "客户姓名为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getCustomerPhone())) {
            throw new BusinessException("", "客户手机号为空");
        }
        if (settleOrderDto.getType() == null) {
            throw new BusinessException("", "结算类型为空");
        }
        if (settleOrderDto.getAmount() == null || settleOrderDto.getAmount() < 0) {
            throw new BusinessException("", "金额不合法");
        }
        //因为订金没有所属部门信息，所以暂时注释掉
        /*if (settleOrderDto.getBusinessDepId() == null) {
            throw new BusinessException("", "业务部门ID为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getBusinessDepName())) {
            throw new BusinessException("", "业务部门名称为空");
        }*/
        if (settleOrderDto.getSubmitterId() == null) {
            throw new BusinessException("", "提交人ID为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getSubmitterName())) {
            throw new BusinessException("", "提交人姓名为空");
        }
        if (settleOrderDto.getSubmitterDepId() == null) {
            throw new BusinessException("", "提交人部门ID为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getSubmitterDepName())) {
            throw new BusinessException("", "提交人部门名称为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getReturnUrl())) {
            throw new BusinessException("", "回调路径为空");
        }
        validParamsSpecial(settleOrderDto);
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        return;
    }
}
