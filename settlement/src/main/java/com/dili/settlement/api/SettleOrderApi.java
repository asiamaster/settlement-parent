package com.dili.settlement.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.settlement.dispatcher.OrderDispatcher;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.EnableEnum;
import com.dili.settlement.enums.ReverseEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.handler.ServiceNameHolder;
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

import java.util.List;

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
    private OrderDispatcher orderDispatcher;

    /**
     * 查询结算单列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list")
    public BaseOutput<List<SettleOrder>> list(@RequestBody SettleOrderDto query) {
        List<SettleOrder> itemList = settleOrderService.list(query);
        return BaseOutput.success().setData(itemList);
    }

    /**
     * 提交结算单接口
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/submit")
    public BaseOutput<SettleOrder> submit(@RequestBody SettleOrderDto settleOrderDto) {
        validateSaveParams(settleOrderDto);
        settleOrderDto.setCode(RpcResultResolver.resolver(uidRpc.bizNumber(settleOrderDto.getMarketCode() + "_settleOrder"), ServiceNameHolder.UID_SERVICE_NAME));
        settleOrderDto.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrderDto.setSubmitTime(DateUtil.nowDateTime());
        settleOrderDto.setDeductEnable(settleOrderDto.getDeductEnable() == null ? EnableEnum.NO.getCode() : settleOrderDto.getDeductEnable());
        settleOrderDto.setReverse(ReverseEnum.NO.getCode());
        SettleOrder settleOrder = orderDispatcher.save(settleOrderDto);
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
        if (StrUtil.isBlank(settleOrderDto.getMarketCode())) {
            throw new BusinessException("", "市场编码为空");
        }
        if (settleOrderDto.getMchId() == null) {
            throw new BusinessException("", "商户ID为空");
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
        if (StrUtil.isBlank(settleOrderDto.getCustomerCertificate())) {
            throw new BusinessException("", "客户证件号为空");
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
            if (feeItem.getChargeItemId() == null) {
                throw new BusinessException("", "费用项ID为空");
            }
            if (StrUtil.isBlank(feeItem.getChargeItemName())) {
                throw new BusinessException("", "费用项名称为空");
            }
            if (feeItem.getAmount() == null || feeItem.getAmount() < 0L) {
                throw new BusinessException("", "费用项金额不合法");
            }
            totalFeeAmount += feeItem.getAmount();
        }
        if (!settleOrderDto.getAmount().equals(totalFeeAmount)) {
            throw new BusinessException("", "结算金额与费用项总额不相符");
        }
    }

    /**
     * 根据结算单id取消
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancelById")
    public BaseOutput<String> cancelById(Long id) {
        if (id == null) {
            return BaseOutput.failure("结算单ID为空");
        }
        settleOrderService.cancelById(id);
        return BaseOutput.success();
    }

    /**
     * 根据结算单编号取消
     * @param code
     * @return
     */
    @RequestMapping(value = "/cancelByCode")
    public BaseOutput<String> cancelByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return BaseOutput.failure("结算单号为空");
        }
        settleOrderService.cancelByCode(code);
        return BaseOutput.success();
    }

    /**
     * 根据appId businessCode取消
     * @param appId 应用ID
     * @param orderCode 订单号
     * @return
     */
    @RequestMapping(value = "/cancel")
    public BaseOutput<String> cancel(Long appId, String orderCode) {
        if (appId == null) {
            return BaseOutput.failure("应用ID为空");
        }
        if (StrUtil.isBlank(orderCode)) {
            return BaseOutput.failure("订单号为空");
        }
        settleOrderService.cancel(appId, orderCode);
        return BaseOutput.success();
    }

    /**
     * 根据id查询结算单
     * @param id
     * @return
     */
    @RequestMapping(value = "/getById")
    public BaseOutput<SettleOrder> getById(Long id) {
        if (id == null) {
            return BaseOutput.failure("ID为空");
        }
        return BaseOutput.success().setData(settleOrderService.get(id));
    }

    /**
     * 根据结算单号查询结算单
     * @param code
     * @return
     */
    @RequestMapping(value = "/getByCode")
    public BaseOutput<SettleOrder> getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return BaseOutput.failure("结算单号为空");
        }
        return BaseOutput.success().setData(settleOrderService.getByCode(code));
    }

    /**
     * 根据appId businessCode 查询结算单
     * @param appId 应用ID
     * @param orderCode 业务编号
     * @return
     */
    @RequestMapping(value = "/get")
    public BaseOutput<SettleOrder> get(Long appId, String orderCode) {
        if (appId == null) {
            return BaseOutput.failure("应用ID为空");
        }
        if (StrUtil.isBlank(orderCode)) {
            return BaseOutput.failure("订单号为空");
        }
        return BaseOutput.success().setData(settleOrderService.get(appId, orderCode));
    }

    /**
     * 用于作废业务接口
     * @param param
     * @return
     */
    @RequestMapping(value = "/invalid")
    public BaseOutput<?> invalid(@RequestBody InvalidRequestDto param) {
        checkInvalidParam(param);
        settleOrderService.invalid(param);
        return BaseOutput.success();
    }

    /**
     * 验证作废请求参数
     * @param param
     */
    private void checkInvalidParam(InvalidRequestDto param) {
        if (param.getMarketId() == null) {
            throw new BusinessException("", "市场ID为空");
        }
        if (StrUtil.isBlank(param.getMarketCode())) {
            throw new BusinessException("", "市场编码为空");
        }
        if (param.getAppId() == null) {
            throw new BusinessException("", "应用ID为空");
        }
        if (param.getOperatorId() == null) {
            throw new BusinessException("", "操作员ID为空");
        }
        if (StrUtil.isBlank(param.getOperatorName())) {
            throw new BusinessException("", "操作员姓名为空");
        }
        if (CollUtil.isEmpty(param.getOrderCodeList())) {
            throw new BusinessException("", "订单号列表为空");
        }
    }
}
