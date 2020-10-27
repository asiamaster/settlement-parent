package com.dili.settlement.component;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.settle.PayService;
import com.dili.settlement.settle.RefundService;
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
public class SettleDispatchHandler {

    @Resource
    private List<PayService> payServiceList;

    @Resource
    private List<RefundService> refundServiceList;

    private Map<Integer, PayService> payServiceMap = new ConcurrentHashMap<>();

    private Map<Integer, RefundService> refundServiceMap = new ConcurrentHashMap<>();

    /**
     * 初始化方法
     */
    @PostConstruct
    public void init() {
        for (PayService validateService : payServiceList) {
            payServiceMap.put(validateService.supportWay(), validateService);
        }

        for (RefundService validateService : refundServiceList) {
            refundServiceMap.put(validateService.supportWay(), validateService);
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
        PayService payService = payServiceMap.get(settleOrderDto.getWay());
        if (payService == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        payService.validSubmitParams(settleOrderDto);
    }

    /**
     * 验证支付参数
     * @param settleOrderDto
     */
    public void validPayParams(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        PayService payService = payServiceMap.get(settleOrderDto.getWay());
        if (payService == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        payService.validSettleParams(settleOrderDto);
    }

    /**
     * 支付
     * @param settleOrder
     * @param settleOrderDto
     */
    public void pay(SettleOrder settleOrder, SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        PayService payService = payServiceMap.get(settleOrderDto.getWay());
        if (payService == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        payService.settle(settleOrder, settleOrderDto);
    }

    /**
     * 验证退款单参数
     * @param settleOrderDto
     */
    public void validRefundOrderParams(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        RefundService refundService = refundServiceMap.get(settleOrderDto.getWay());
        if (refundService == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        refundService.validSubmitParams(settleOrderDto);
    }

    /**
     * 验证退款参数
     * @param settleOrderDto
     */
    public void validRefundParams(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        RefundService refundService = refundServiceMap.get(settleOrderDto.getWay());
        if (refundService == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        refundService.validSettleParams(settleOrderDto);
    }

    /**
     * 验证退款参数
     * @param settleOrder
     * @param settleOrderDto
     */
    public void refund(SettleOrder settleOrder, SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        RefundService refundService = refundServiceMap.get(settleOrderDto.getWay());
        if (refundService == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        refundService.settle(settleOrder, settleOrderDto);
    }

    /**
     * 作废支付单
     * @param po
     */
    public void payInvalid(SettleOrder po, InvalidRequestDto param) {
        if (po.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        PayService payService = payServiceMap.get(po.getWay());
        if (payService == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        payService.invalid(po, param);
    }
}
