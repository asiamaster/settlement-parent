package com.dili.settlement.component;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.validation.PayValidateService;
import com.dili.settlement.validation.RefundValidateService;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 结算验证分发器
 */
@Component
public class SettleValidateDispatchHandler {

    @Resource
    private List<PayValidateService> payValidateServiceList;

    @Resource
    private List<RefundValidateService> refundValidateServiceList;

    private Map<Integer, PayValidateService> payValidateServiceMap = new ConcurrentHashMap<>();

    private Map<Integer, RefundValidateService> refundValidateServiceMap = new ConcurrentHashMap<>();

    /**
     * 初始化方法
     */
    @PostConstruct
    public void init() {
        for (PayValidateService validateService : payValidateServiceList) {
            payValidateServiceMap.put(validateService.supportWay(), validateService);
        }

        for (RefundValidateService validateService : refundValidateServiceList) {
            refundValidateServiceMap.put(validateService.supportWay(), validateService);
        }
    }

    /**
     * 验证缴费单提交参数
     * @param settleOrderDto
     */
    public void validPayOrderParams(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        PayValidateService validateService = payValidateServiceMap.get(settleOrderDto.getWay());
        if (validateService == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        validateService.validSubmitParams(settleOrderDto);
    }

    /**
     * 验证支付参数
     * @param settleOrderDto
     */
    public void validPayParams(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        PayValidateService validateService = payValidateServiceMap.get(settleOrderDto.getWay());
        if (validateService == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        validateService.validSettleParams(settleOrderDto);
    }

    /**
     * 验证退款单参数
     * @param settleOrderDto
     */
    public void validRefundOrderParams(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        RefundValidateService validateService = refundValidateServiceMap.get(settleOrderDto.getWay());
        if (validateService == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        validateService.validSubmitParams(settleOrderDto);
    }

    /**
     * 验证退款参数
     * @param settleOrderDto
     */
    public void validRefundParams(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        RefundValidateService validateService = refundValidateServiceMap.get(settleOrderDto.getWay());
        if (validateService == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        validateService.validSettleParams(settleOrderDto);
    }
}
