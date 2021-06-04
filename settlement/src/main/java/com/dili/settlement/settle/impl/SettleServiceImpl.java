package com.dili.settlement.settle.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.rpc.BusinessChargeItemRpc;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.customer.sdk.domain.CharacterType;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.settlement.config.SerialMQConfig;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.*;
import com.dili.settlement.dto.pay.FeeItemDto;
import com.dili.settlement.dto.pay.TradeResponseDto;
import com.dili.settlement.enums.ActionEnum;
import com.dili.settlement.enums.BizTypeEnum;
import com.dili.settlement.enums.ReverseEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.handler.ServiceNameHolder;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.rpc.AccountQueryRpc;
import com.dili.settlement.rpc.PayRpc;
import com.dili.settlement.service.CustomerAccountService;
import com.dili.settlement.service.RetryRecordService;
import com.dili.settlement.service.SettleFeeItemService;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.settle.SettleService;
import com.dili.settlement.task.AsyncTaskExecutor;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uid.sdk.rpc.feign.UidFeignRpc;
import com.diligrp.message.sdk.domain.input.MessageInfoInput;
import com.diligrp.message.sdk.rpc.SmsMessageRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 结算验证基础实现类
 */
public abstract class SettleServiceImpl implements SettleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleServiceImpl.class);

    @Autowired
    protected SettleOrderService settleOrderService;

    @Autowired
    protected CustomerAccountService customerAccountService;

    @Autowired
    protected RetryRecordService retryRecordService;

    @Autowired
    protected SettleFeeItemService settleFeeItemService;

    @Autowired
    protected PayRpc payRpc;

    @Autowired
    protected UidFeignRpc uidFeignRpc;

    @Autowired
    protected AccountQueryRpc accountQueryRpc;

    @Autowired
    protected BusinessChargeItemRpc businessChargeItemRpc;

    @Autowired
    protected RabbitMQMessageService rabbitMQMessageService;

    @Autowired
    protected CustomerRpc customerRpc;

    @Autowired
    private SmsMessageRpc smsMessageRpc;

    @Override
    public List<SettleOrder> canSettle(List<Long> ids) {
        //查询并锁定
        List<SettleOrder> settleOrderList = settleOrderService.lockList(ids);
        if (ids.size() != settleOrderList.size()) {
            throw new BusinessException("", "结算单记录已变更,请刷新重试");
        }
        if (!settleOrderList.stream().allMatch(temp -> temp.getState().equals(SettleStateEnum.WAIT_DEAL.getCode()))) {
            throw new BusinessException("", "结算单状态已变更,请刷新重试");
        }
        return settleOrderList;
    }

    @Override
    public void buildSettleInfo(SettleOrder settleOrder, SettleOrderDto settleOrderDto, LocalDateTime localDateTime) {
        settleOrder.setState(SettleStateEnum.DEAL.getCode());
        settleOrder.setWay(settleOrderDto.getWay());
        settleOrder.setOperatorId(settleOrderDto.getOperatorId());
        settleOrder.setOperatorName(settleOrderDto.getOperatorName());
        settleOrder.setOperateTime(localDateTime);
        settleOrder.setNotes(settleOrderDto.getNotes());
        buildSettleInfoSpecial(settleOrder, settleOrderDto, localDateTime);
    }

    @Override
    public void buildSettleInfoSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto, LocalDateTime localDateTime) {
        return;
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        return;
    }

    @Override
    public void settleSpecial(List<SettleOrder> settleOrderList, SettleOrderDto settleOrderDto) {
        return;
    }

    @Override
    public void validParams(SettleOrderDto settleOrderDto) {
        validParamsSpecial(settleOrderDto);
    }

    @Override
    public void invalid(SettleOrder po, InvalidRequestDto param) {
        SettleOrder reverseOrder = new SettleOrder();
        BeanUtil.copyProperties(po, reverseOrder);
        reverseOrder.setId(null);
        reverseOrder.setCode(RpcResultResolver.resolver(uidFeignRpc.getBizNumber(param.getMarketCode() + "_settleOrder"), ServiceNameHolder.UID_SERVICE_NAME));
        reverseOrder.setOrderCode(po.getCode());
        reverseOrder.setOperatorId(param.getOperatorId());
        reverseOrder.setOperatorName(param.getOperatorName());
        reverseOrder.setOperateTime(DateUtil.nowDateTime());
        reverseOrder.setReverse(ReverseEnum.YES.getCode());
        settleOrderService.insertSelective(reverseOrder);

        invalidSpecial(po, reverseOrder, param);
    }

    @Override
    public void invalidSpecial(SettleOrder po, SettleOrder reverseOrder, InvalidRequestDto param) {
        return;
    }

    @Override
    public void createAccountSerial(SettleOrderDto settleOrderDto, TradeResponseDto tradeResponse, String tradeId) {
        try {
            List<Long> idList = tradeResponse.getStreams().stream().map(FeeItemDto::getType).collect(Collectors.toList());
            Map<Long, BusinessChargeItemDto> businessChargeItemMap = findChargeItemByIdList(idList);
            List<SerialRecordDto> serialRecordList = new ArrayList<>();
            long totalFrozenBalance = tradeResponse.getFrozenBalance() + tradeResponse.getFrozenAmount();
            for (FeeItemDto feeItem : tradeResponse.getStreams()) {
                String[] arr = StrUtil.isBlank(feeItem.getDescription()) ? new String[2] : feeItem.getDescription().split("\\|");
                SerialRecordDto serialRecord = new SerialRecordDto();
                serialRecord.setType(arr[0]);
                serialRecord.setTypeName(BizTypeEnum.getNameByCode(arr[0]));
                serialRecord.setAccountId(settleOrderDto.getTradeAccountId());
                serialRecord.setCardNo(settleOrderDto.getTradeCardNo());
                serialRecord.setCustomerId(settleOrderDto.getTradeCustomerId());
                serialRecord.setCustomerNo(settleOrderDto.getTradeCustomerCode());
                serialRecord.setCustomerName(settleOrderDto.getTradeCustomerName());
                serialRecord.setCustomerType(buildCustomerCharacterType(settleOrderDto.getTradeCustomerId(), settleOrderDto.getMarketId()));
                serialRecord.setAction(feeItem.getAmount() == null ? null : feeItem.getAmount() < 0L ? ActionEnum.EXPENSE.getCode() : ActionEnum.INCOME.getCode());
                serialRecord.setStartBalance(countStartBalance(feeItem.getBalance(), totalFrozenBalance));
                serialRecord.setAmount(feeItem.getAmount() == null ? null : Math.abs(feeItem.getAmount()));
                serialRecord.setEndBalance(countEndBalance(serialRecord.getStartBalance(), feeItem.getAmount()));
                //serialRecord.setTradeType();
                serialRecord.setTradeChannel(getTradeChannel());
                serialRecord.setTradeNo(tradeId);
                BusinessChargeItemDto businessChargeItem = businessChargeItemMap.get(feeItem.getType());
                serialRecord.setFundItem(businessChargeItem != null ? businessChargeItem.getFundItem() : null);
                serialRecord.setFundItemName(businessChargeItem != null ? businessChargeItem.getFundItemValue() : null);
                serialRecord.setOperatorId(settleOrderDto.getOperatorId());
                serialRecord.setOperatorNo(settleOrderDto.getOperatorNo());
                serialRecord.setOperatorName(settleOrderDto.getOperatorName());
                serialRecord.setOperateTime(tradeResponse.getWhen());
                serialRecord.setNotes(arr[1]);
                serialRecord.setFirmId(settleOrderDto.getMarketId());
                serialRecord.setHoldName(settleOrderDto.getHoldName());
                serialRecord.setHoldCertificateNumber(settleOrderDto.getHoldCertificateNumber());
                serialRecord.setHoldContactsPhone(settleOrderDto.getHoldContactsPhone());
                serialRecordList.add(serialRecord);
            }
            rabbitMQMessageService.send(SerialMQConfig.EXCHANGE_ACCOUNT_SERIAL, SerialMQConfig.ROUTING_ACCOUNT_SERIAL, JSON.toJSONString(serialRecordList), null, null);
        } catch (Exception e) {
            LOGGER.error("create serial error", e);
            LOGGER.info("settleOrder: {}，tradeResponse: {}", JSON.toJSONString(settleOrderDto), JSON.toJSONString(tradeResponse));
        }
    }

    /**
     * 用于处理支付费用项添加 为0则不添加
     * @param feeItemList
     * @param item
     */
    protected void addFeeItem(List<FeeItemDto> feeItemList, FeeItemDto item) {
        if (item.getAmount() == 0L) {
            return;
        }
        feeItemList.add(item);
    }

    /**
     * 查询收费项 返回单一值
     * @param marketId
     * @param businessType
     * @param code
     * @return
     */
    protected BusinessChargeItemDto findOneChargeItem(Long marketId, String businessType, String code) {
        BusinessChargeItemDto query = new BusinessChargeItemDto();
        query.setMarketId(marketId);
        query.setBusinessType(businessType);
        query.setCode(code);
        List<BusinessChargeItemDto> chargeItemList = RpcResultResolver.resolver(businessChargeItemRpc.listByExample(query), ServiceNameHolder.ASSETS_SERVICE_NAME);
        return CollUtil.isEmpty(chargeItemList) ? null : chargeItemList.get(0);
    }

    /**
     * 根据费用项ID列表查询并转换为map
     * @param idList
     * @return
     */
    protected Map<Long, BusinessChargeItemDto> findChargeItemByIdList(List<Long> idList) {
        BusinessChargeItemDto query = new BusinessChargeItemDto();
        query.setIdList(idList);
        List<BusinessChargeItemDto> chargeItemList = RpcResultResolver.resolver(businessChargeItemRpc.listByExample(query), ServiceNameHolder.ASSETS_SERVICE_NAME);
        Map<Long, BusinessChargeItemDto> businessChargeItemMap = new ConcurrentHashMap<>();
        for (BusinessChargeItemDto temp : chargeItemList) {
            businessChargeItemMap.put(temp.getId(), temp);
        }
        return businessChargeItemMap;
    }

    /**
     * 查询收费项 返回单一值
     * @param id
     * @return
     */
    protected BusinessChargeItemDto getChargeItemById(Long id) {
        return RpcResultResolver.resolver(businessChargeItemRpc.getById(id), ServiceNameHolder.ASSETS_SERVICE_NAME);
    }

    /**
     *
     * @param customerId
     * @return
     */
    protected String buildCustomerCharacterType(Long customerId, Long marketId) {
        try {
            CustomerExtendDto customerExtendDto = RpcResultResolver.resolver(customerRpc.get(customerId, marketId), ServiceNameHolder.CUSTOMER_SERVICE_NAME);
            String type = customerExtendDto.getCharacterTypeList().stream().map(CharacterType::getSubType).collect(Collectors.joining(","));
            return type;
        } catch (Exception e) {
            LOGGER.error("query customer info", e);
        }
        return "";
    }

    /**
     * 异步发送手机短信
     * @param marketCode
     * @param accountId
     * @param localDateTime
     * @param amount
     * @param tradeType
     * @param balance
     */
    protected void asyncSendMessage(String marketCode, Long accountId, LocalDateTime localDateTime, Long amount, String tradeType, Long balance) {
        try {
            AsyncTaskExecutor.submit(() -> {
                boolean result;
                UserAccountCardResponseDto account = RpcResultResolver.resolver(accountQueryRpc.findSingle(UserAccountSingleQueryDto.newDto(accountId)), ServiceNameHolder.ACCOUNT_SERVICE_NAME);
                if (account != null) {
                    Map<String, String> params = new HashMap<>();
                    params.put("tradeCard", account.getCardNo().substring(account.getCardNo().length() - 4));
                    params.put("tradeTime", DateUtil.formatDateTime(localDateTime, "MM月dd日 HH:mm"));
                    params.put("amount", MoneyUtils.centToYuan(Math.abs(amount)));
                    params.put("tradeType", tradeType);
                    params.put("balance", MoneyUtils.centToYuan(balance + amount));
                    MessageInfoInput message = new MessageInfoInput();
                    message.setMarketCode(marketCode);
                    message.setBusinessMarketCode(marketCode);
                    message.setSystemCode("toll");
                    message.setSceneCode("consumerNotice");
                    message.setCellphone(account.getCustomerContactsPhone());
                    message.setParameters(JSON.toJSONString(params));
                    BaseOutput baseOutput = smsMessageRpc.receiveMessage(message);
                    if (!baseOutput.isSuccess()) {
                        LOGGER.error("Send phone message error, result is " + JSON.toJSONString(baseOutput));
                    }
                    result = baseOutput.isSuccess();
                } else {
                    result = false;
                }
                return result;
            });
        } catch (Exception e) {
            LOGGER.error("Send phone message error", e);
        }
    }
}
