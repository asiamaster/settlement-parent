package com.dili.settlement.api;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.enums.EditEnableEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.service.CodeService;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 结算单相关api
 */
@RestController
@RequestMapping(value = "/api/settleOrder")
public class SettleOrderApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleOrderApi.class);

    @Resource
    private CodeService codeService;

    @Resource
    private SettleOrderService settleOrderService;

    @RequestMapping(value = "/save")
    public BaseOutput<SettleOrder> save(@RequestBody SettleOrder settleOrder) {
        try {
            validSaveParameters(settleOrder);
            settleOrder.setCode(codeService.generate());
            settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
            settleOrder.setSubmitTime(DateUtil.nowDateTime());
            settleOrder.setEditEnable(settleOrder.getEditEnable() == null ? EditEnableEnum.YES.getCode() : settleOrder.getEditEnable());
            settleOrderService.save();
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOGGER.error("method save", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 验证保存接口参数
     * @param settleOrder
     */
    private void validSaveParameters(SettleOrder settleOrder) {
        if (settleOrder.getMarketId() == null) {
            throw new BusinessException("", "市场ID为空");
        }
        if (settleOrder.getAppId() == null) {
            throw new BusinessException("", "应用ID为空");
        }
        if (settleOrder.getBusinessType() == null) {
            throw new BusinessException("", "业务类型为空");
        }
        if (StrUtil.isBlank(settleOrder.getBusinessCode())) {
            throw new BusinessException("", "业务编码为空");
        }
        if (settleOrder.getCustomerId() == null) {
            throw new BusinessException("", "客户ID为空");
        }
        if (StrUtil.isBlank(settleOrder.getCustomerName())) {
            throw new BusinessException("", "客户姓名为空");
        }
        if (StrUtil.isBlank(settleOrder.getCustomerPhone())) {
            throw new BusinessException("", "客户手机号为空");
        }
        if (settleOrder.getType() == null) {
            throw new BusinessException("", "结算类型为空");
        }
        if (settleOrder.getAmount() == null || settleOrder.getAmount() < 0) {
            throw new BusinessException("", "金额不合法");
        }
        if (settleOrder.getBusinessDepId() == null) {
            throw new BusinessException("", "业务部门ID为空");
        }
        if (StrUtil.isBlank(settleOrder.getBusinessDepName())) {
            throw new BusinessException("", "业务部门名称为空");
        }
        if (settleOrder.getSubmitterId() == null) {
            throw new BusinessException("", "提交人ID为空");
        }
        if (StrUtil.isBlank(settleOrder.getSubmitterName())) {
            throw new BusinessException("", "提交人姓名为空");
        }
        if (settleOrder.getSubmitterDepId() == null) {
            throw new BusinessException("", "提交人部门ID为空");
        }
        if (StrUtil.isBlank(settleOrder.getBusinessDepName())) {
            throw new BusinessException("", "提交人部门名称为空");
        }
        if (StrUtil.isBlank(settleOrder.getReturnUrl())) {
            throw new BusinessException("", "回调路径为空");
        }
    }
}
