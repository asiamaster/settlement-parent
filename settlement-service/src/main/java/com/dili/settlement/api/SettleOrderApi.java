package com.dili.settlement.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.settlement.dispatcher.PayDispatcher;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.EnableEnum;
import com.dili.settlement.enums.ReverseEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.rpc.UidRpc;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 结算单相关api
 */
@RestController
@RequestMapping(value = "/api/settleOrder")
public class SettleOrderApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleOrderApi.class);

    @Autowired
    private UidRpc uidRpc;

    @Autowired
    private SettleOrderService settleOrderService;

    @Autowired
    private PayDispatcher settleDispatchHandler;
    /**
     * 提交结算单接口
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/save")
    public BaseOutput<SettleOrder> save(@RequestBody SettleOrderDto settleOrderDto) {
        validateSaveParams(settleOrderDto);
        settleOrderDto.setCode(RpcResultResolver.resolver(uidRpc.bizNumber(settleOrderDto.getMarketCode() + "_settleOrder"), "uid-service"));
        settleOrderDto.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrderDto.setSubmitTime(DateUtil.nowDateTime());
        settleOrderDto.setDeductEnable(settleOrderDto.getDeductEnable() == null ? EnableEnum.NO.getCode() : settleOrderDto.getDeductEnable());
        settleOrderDto.setReverse(ReverseEnum.NO.getCode());
        SettleOrder settleOrder = settleOrderService.save(settleOrderDto);
        return BaseOutput.success().setData(settleOrder);
    }

    /**
     * 验证提交接口的参数
     * @param settleOrderDto
     */
    private void validateSaveParams(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getMarketId() == null) {
            throw new BusinessException("", "市场ID为空");
        }
        if (settleOrderDto.getMchId() == null) {
            throw new BusinessException("", "商户ID为空");
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
        if (StrUtil.isBlank(settleOrderDto.getOrderCode())) {
            throw new BusinessException("", "订单号为空");
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
        if (settleOrderDto.getAmount() == null || settleOrderDto.getAmount() < 0L) {
            throw new BusinessException("", "金额不合法");
        }
        if (settleOrderDto.getSubmitterId() == null) {
            throw new BusinessException("", "提交人ID为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getSubmitterName())) {
            throw new BusinessException("", "提交人姓名为空");
        }
        if (CollUtil.isEmpty(settleOrderDto.getSettleFeeItemList())) {
            throw new BusinessException("", "费用项列表为空");
        }
        if (CollUtil.isEmpty(settleOrderDto.getSettleOrderLinkList())) {
            throw new BusinessException("", "链接列表为空");
        }
        long totalFeeAmount = 0L;
        for (SettleFeeItem feeItem : settleOrderDto.getSettleFeeItemList()) {
            if (feeItem.getAmount() == null || feeItem.getAmount() < 0L) {
                throw new BusinessException("", "费用项金额不合法");
            }
            totalFeeAmount += feeItem.getAmount();
        }
        if (!settleOrderDto.getAmount().equals(totalFeeAmount)) {
            throw new BusinessException("", "结算金额与费用项总额不相等");
        }
    }
}
