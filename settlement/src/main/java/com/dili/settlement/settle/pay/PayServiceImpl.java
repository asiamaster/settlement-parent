package com.dili.settlement.settle.pay;

import cn.hutool.core.bean.BeanUtil;
import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.settlement.component.MchIdHolder;
import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.settlement.domain.RetryRecord;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.AccountMergeDto;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleDataDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.*;
import com.dili.settlement.enums.*;
import com.dili.settlement.handler.ServiceNameHolder;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.settle.PayService;
import com.dili.settlement.settle.impl.SettleServiceImpl;
import com.dili.settlement.util.DateUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付数据验证基础类
 */
public abstract class PayServiceImpl extends SettleServiceImpl implements PayService {

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "pay/special";
    }

    @GlobalTransactional
    @Transactional
    @Override
    public List<SettleOrder> settle(SettleOrderDto settleOrderDto) {
        //验证参数
        validParams(settleOrderDto);

        //验证单据并锁定
        List<SettleOrder> settleOrderList = canSettle(settleOrderDto.getIdList());
        //构建结算信息
        SettleDataDto settleDataDto = prepareSettleData(settleOrderList, settleOrderDto);
        if (settleOrderDto.getSettleAmountDto().getTotalAmount() == 0L) {//结算总额为0则不走支付
            //存储重试记录
            retryRecordService.batchInsert(settleDataDto.getRetryRecordList());
            settleOrderService.batchSettleUpdate(settleOrderList, "");
            //结算特殊处理接口
            settleSpecial(settleOrderList, settleOrderDto);
        } else {
            MchIdHolder.set(settleOrderDto.getMchId());
            //创建交易
            CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto.build(TradeTypeEnum.SYNTHESIZE.getCode(), getTradeFundAccountId(settleOrderDto), settleOrderDto.getSettleAmountDto().getTotalAmount(), "", "");
            CreateTradeResponseDto createTradeResponseDto = RpcResultResolver.resolver(payRpc.prepareTrade(createTradeRequestDto), ServiceNameHolder.PAY_SERVICE_NAME);
            //操作定金账户
            Map<Long, AccountMergeDto> earnestMap = settleDataDto.getEarnestMap();
            if (!earnestMap.isEmpty()) {
                earnestMap.forEach((key, value) -> customerAccountService.handleEarnest(value.getMchId(), value.getCustomerId(), value.getAmount(), value.getFrozenAmount(), value.getSerialList()));
            }
            //操作转抵账户
            Map<Long, AccountMergeDto> transferMap = settleDataDto.getTransferMap();
            if (!transferMap.isEmpty()){
                transferMap.forEach((key, value) -> customerAccountService.handleTransfer(value.getMchId(), value.getCustomerId(), value.getAmount(), value.getFrozenAmount(), value.getSerialList()));
            }
            //存储重试记录
            retryRecordService.batchInsert(settleDataDto.getRetryRecordList());
            //修改结算单
            settleOrderService.batchSettleUpdate(settleOrderList, createTradeResponseDto.getTradeId());
            //结算特殊处理接口
            settleSpecial(settleOrderList, settleOrderDto);
            //提交交易
            TradeRequestDto tradeRequest = TradeRequestDto.build(createTradeResponseDto.getTradeId(), getTradeFundAccountId(settleOrderDto), getTradeChannel(), settleOrderDto.getTradePassword(), settleDataDto.getFeeItemList(), settleDataDto.getDeductFeeItemList());
            TradeResponseDto tradeResponseDto = RpcResultResolver.resolver(payRpc.commitTrade(tradeRequest), ServiceNameHolder.PAY_SERVICE_NAME);

            MchIdHolder.clear();
            //保存流水
            createAccountSerial(settleOrderDto, tradeResponseDto, createTradeResponseDto.getTradeId());

            //如果是账户支付则发送短信
            if (getTradeChannel().equals(TradeChannelEnum.BALANCE.getCode())) {
                asyncSendMessage(settleOrderDto.getMarketCode(), settleOrderDto.getTradeAccountId(), tradeResponseDto.getWhen(), tradeResponseDto.getAmount(), "扣款", tradeResponseDto.getBalance());
            }
        }
        return settleOrderList;
    }

    /**
     * 准备结算数据
     *
     * @param settleOrderList
     * @param settleOrderDto
     * @return
     */
    @Override
    public SettleDataDto prepareSettleData(List<SettleOrder> settleOrderList, SettleOrderDto settleOrderDto) {
        LocalDateTime localDateTime = DateUtil.nowDateTime();

        SettleDataDto settleDataDto = new SettleDataDto();
        //设置结算相关信息 构建重试记录 转换map
        List<RetryRecord> retryRecordList = new ArrayList<>(settleOrderList.size());
        Map<Long, SettleOrder> settleOrderMap = new HashMap<>(settleOrderList.size());
        for (SettleOrder settleOrder : settleOrderList) {//构建结算单信息以及回调记录列表
            buildSettleInfo(settleOrder, settleOrderDto, localDateTime);
            retryRecordList.add(RetryRecord.build(settleOrder.getId()));
            settleOrderMap.put(settleOrder.getId(), settleOrder);
        }
        //构建费用项 抵扣项 定金与转抵账户信息
        List<SettleFeeItem> settleFeeItemList = settleFeeItemService.listBySettleOrderIdList(settleOrderDto.getIdList());
        Map<Long, AccountMergeDto> earnestMap = new HashMap<>(settleOrderList.size());
        Map<Long, AccountMergeDto> transferMap = new HashMap<>(settleOrderList.size());
        List<FeeItemDto> feeItemList = new ArrayList<>();
        List<FeeItemDto> deductFeeItemList = new ArrayList<>();
        for (SettleFeeItem settleFeeItem : settleFeeItemList) {
            if (settleFeeItem.getAmount() == 0L) {//金额为0不提交到支付、不构建流水、不累计定金
                continue;
            }
            SettleOrder temp = settleOrderMap.get(settleFeeItem.getSettleOrderId());
            if (settleFeeItem.getChargeItemType().equals(ChargeItemTypeEnum.FEE.getCode())) {
                addFeeItem(feeItemList, FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getChargeItemId(), settleFeeItem.getChargeItemName(), String.format("%s|%s单号%s", temp.getBusinessType(), BizTypeEnum.getNameByCode(temp.getBusinessType()), temp.getBusinessCode())));
                if (BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(settleFeeItem.getFeeType())) {//费用项包含定金
                    AccountMergeDto accountMergeDto = earnestMap.get(temp.getCustomerId());
                    accountMergeDto = accountMergeDto == null ? AccountMergeDto.build(temp.getMchId(), temp.getCustomerId()) : accountMergeDto;
                    accountMergeDto.addAmount(settleFeeItem.getAmount(), CustomerAccountSerial.build(settleFeeItem.getFeeType(), ActionEnum.INCOME.getCode(), SceneEnum.PAYMENT.getCode(), settleFeeItem.getAmount(), localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), settleFeeItem.getSettleOrderCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), temp.getBusinessCode()));
                    earnestMap.put(temp.getCustomerId(), accountMergeDto);
                }
            }
            if (settleFeeItem.getChargeItemType().equals(ChargeItemTypeEnum.DEDUCT.getCode())) {
                addFeeItem(deductFeeItemList, FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getChargeItemId(), settleFeeItem.getChargeItemName(), String.format("%s|%s抵扣充值，关联%s单号%s", temp.getBusinessType(), settleFeeItem.getFeeName(), BizTypeEnum.getNameByCode(temp.getBusinessType()), temp.getBusinessCode())));
                if (BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(settleFeeItem.getFeeType())) {//抵扣项包含定金
                    AccountMergeDto accountMergeDto = earnestMap.get(temp.getCustomerId());
                    accountMergeDto = accountMergeDto == null ? AccountMergeDto.build(temp.getMchId(), temp.getCustomerId()) : accountMergeDto;
                    accountMergeDto.subAmount(settleFeeItem.getAmount(), CustomerAccountSerial.build(settleFeeItem.getFeeType(), ActionEnum.EXPENSE.getCode(), SceneEnum.DEDUCT.getCode(), settleFeeItem.getAmount(), localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), settleFeeItem.getSettleOrderCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), temp.getBusinessCode()));
                    accountMergeDto.subFrozenAmount(settleFeeItem.getAmount());
                    earnestMap.put(temp.getCustomerId(), accountMergeDto);
                }
                if (BusinessChargeItemEnum.SystemSubjectType.转抵.getCode().equals(settleFeeItem.getFeeType())) {//抵扣项包含转抵
                    AccountMergeDto accountMergeDto = transferMap.get(temp.getCustomerId());
                    accountMergeDto = accountMergeDto == null ? AccountMergeDto.build(temp.getMchId(), temp.getCustomerId()) : accountMergeDto;
                    accountMergeDto.subAmount(settleFeeItem.getAmount(), CustomerAccountSerial.build(settleFeeItem.getFeeType(), ActionEnum.EXPENSE.getCode(), SceneEnum.DEDUCT.getCode(), settleFeeItem.getAmount(), localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), settleFeeItem.getSettleOrderCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), temp.getBusinessCode()));
                    accountMergeDto.subFrozenAmount(settleFeeItem.getAmount());
                    transferMap.put(temp.getCustomerId(), accountMergeDto);
                }
            }
        }
        //设置值
        settleDataDto.setEarnestMap(earnestMap);
        settleDataDto.setTransferMap(transferMap);
        settleDataDto.setRetryRecordList(retryRecordList);
        settleDataDto.setFeeItemList(feeItemList);
        settleDataDto.setDeductFeeItemList(deductFeeItemList);
        return settleDataDto;
    }

    @Override
    public void buildSettleInfoSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto, LocalDateTime localDateTime) {
        settleOrder.setSerialNumber(settleOrderDto.getSerialNumber());
        settleOrder.setChargeDate(settleOrderDto.getChargeDate());
    }

    @Override
    public Long getTradeFundAccountId(SettleOrderDto settleOrderDto) {
        return 0L;
    }

    @Override
    public void invalidSpecial(SettleOrder po, SettleOrder reverseOrder, InvalidRequestDto param) {
        if (po.getAmount() == 0L) {
            return;
        }
        //构建费用项 抵扣项 定金与转抵账户信息
        List<SettleFeeItem> settleFeeItemList = settleFeeItemService.listBySettleOrderId(po.getId());
        Map<Long, AccountMergeDto> earnestMap = new HashMap<>(1);
        Map<Long, AccountMergeDto> transferMap = new HashMap<>(1);
        List<FeeItemDto> feeItemList = new ArrayList<>();
        List<FeeItemDto> deductFeeItemList = new ArrayList<>();
        for (SettleFeeItem settleFeeItem : settleFeeItemList) {
            if (settleFeeItem.getChargeItemType().equals(ChargeItemTypeEnum.FEE.getCode())) {
                addFeeItem(feeItemList, FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getChargeItemId(), settleFeeItem.getChargeItemName(), String.format("%s|作废，%s单号%s", po.getBusinessType(), BizTypeEnum.getNameByCode(po.getBusinessType()), po.getBusinessCode())));
                if (BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(settleFeeItem.getFeeType())) {
                    AccountMergeDto accountMergeDto = earnestMap.get(po.getCustomerId());
                    accountMergeDto = accountMergeDto == null ? AccountMergeDto.build(po.getMchId(), po.getCustomerId()) : accountMergeDto;
                    accountMergeDto.subAmount(settleFeeItem.getAmount(), CustomerAccountSerial.build(settleFeeItem.getFeeType(), ActionEnum.EXPENSE.getCode(), SceneEnum.INVALID_OUT.getCode(), settleFeeItem.getAmount(), reverseOrder.getOperateTime(), reverseOrder.getOperatorId(), reverseOrder.getOperatorName(), reverseOrder.getCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), po.getBusinessCode()));
                    earnestMap.put(po.getCustomerId(), accountMergeDto);
                }
            }
            if (settleFeeItem.getChargeItemType().equals(ChargeItemTypeEnum.DEDUCT.getCode())) {
                addFeeItem(deductFeeItemList, FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getChargeItemId(), settleFeeItem.getChargeItemName(), String.format("%s|作废，%s单号%s", po.getBusinessType(), BizTypeEnum.getNameByCode(po.getBusinessType()), po.getBusinessCode())));
                if (BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(settleFeeItem.getFeeType())) {//抵扣项包含定金
                    AccountMergeDto accountMergeDto = earnestMap.get(po.getCustomerId());
                    accountMergeDto = accountMergeDto == null ? AccountMergeDto.build(po.getMchId(), po.getCustomerId()) : accountMergeDto;
                    accountMergeDto.addAmount(settleFeeItem.getAmount(), CustomerAccountSerial.build(settleFeeItem.getFeeType(), ActionEnum.INCOME.getCode(), SceneEnum.INVALID_IN.getCode(), settleFeeItem.getAmount(), reverseOrder.getOperateTime(), reverseOrder.getOperatorId(), reverseOrder.getOperatorName(), reverseOrder.getCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), po.getBusinessCode()));
                    earnestMap.put(po.getCustomerId(), accountMergeDto);
                }
                if (BusinessChargeItemEnum.SystemSubjectType.转抵.getCode().equals(settleFeeItem.getFeeType())) {//抵扣项包含转抵
                    AccountMergeDto accountMergeDto = transferMap.get(po.getCustomerId());
                    accountMergeDto = accountMergeDto == null ? AccountMergeDto.build(po.getMchId(), po.getCustomerId()) : accountMergeDto;
                    accountMergeDto.addAmount(settleFeeItem.getAmount(), CustomerAccountSerial.build(settleFeeItem.getFeeType(), ActionEnum.INCOME.getCode(), SceneEnum.INVALID_IN.getCode(), settleFeeItem.getAmount(), reverseOrder.getOperateTime(), reverseOrder.getOperatorId(), reverseOrder.getOperatorName(), reverseOrder.getCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), po.getBusinessCode()));
                    transferMap.put(po.getCustomerId(), accountMergeDto);
                }
            }
        }
        //操作定金账户
        if (!earnestMap.isEmpty()) {
            earnestMap.forEach((key, value) -> customerAccountService.handleEarnest(value.getMchId(), value.getCustomerId(), value.getAmount(), value.getFrozenAmount(), value.getSerialList()));
        }
        //操作转抵账户
        if (!transferMap.isEmpty()){
            transferMap.forEach((key, value) -> customerAccountService.handleTransfer(value.getMchId(), value.getCustomerId(), value.getAmount(), value.getFrozenAmount(), value.getSerialList()));
        }

        MchIdHolder.set(po.getMchId());
        //提交交易 由于支付是批量操作，作废可能只针对单条，所以无法走支付作废接口，走支付提供的综合退费的接口
        RefundRequestDto refundRequestDto = RefundRequestDto.build(po.getTradeNo(), po.getAmount(), feeItemList, deductFeeItemList);
        TradeResponseDto tradeResponseDto = RpcResultResolver.resolver(payRpc.refundTrade(refundRequestDto), ServiceNameHolder.PAY_SERVICE_NAME);

        MchIdHolder.clear();
        //保存流水
        SettleOrderDto settleOrderDto = new SettleOrderDto();
        BeanUtil.copyProperties(po, settleOrderDto);
        settleOrderDto.setOperatorNo(param.getOperatorNo());
        createAccountSerial(settleOrderDto, tradeResponseDto, po.getTradeNo());

        //如果是账户支付则发送短信
        if (po.getWay().equals(SettleWayEnum.CARD.getCode())) {
            asyncSendMessage(param.getMarketCode(), po.getTradeAccountId(), tradeResponseDto.getWhen(), tradeResponseDto.getAmount(), "收款", tradeResponseDto.getBalance());
        }
    }
}
