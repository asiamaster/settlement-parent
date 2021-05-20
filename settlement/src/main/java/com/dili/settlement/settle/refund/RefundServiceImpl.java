package com.dili.settlement.settle.refund;

import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.settlement.component.MchIdHolder;
import com.dili.settlement.domain.*;
import com.dili.settlement.dto.AccountMergeDto;
import com.dili.settlement.dto.SettleDataDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.*;
import com.dili.settlement.enums.*;
import com.dili.settlement.handler.ServiceNameHolder;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.service.RetryRecordService;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.service.TransferDetailService;
import com.dili.settlement.settle.RefundService;
import com.dili.settlement.settle.impl.SettleServiceImpl;
import com.dili.settlement.util.DateUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 退款数据验证基础实现类
 */
public abstract class RefundServiceImpl extends SettleServiceImpl implements RefundService {

    @Autowired
    protected SettleOrderService settleOrderService;
    @Autowired
    protected RetryRecordService retryRecordService;
    @Autowired
    protected TransferDetailService transferDetailService;

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "refund/special";
    }

    @Override
    public void validParams(SettleOrderDto settleOrderDto) {
        validParamsSpecial(settleOrderDto);
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
        if (settleOrderDto.getSettleAmountDto().getTotalAmount() == 0L) {//结算总额为0则不走支付，也不存在抵扣
            //存储重试记录
            retryRecordService.batchInsert(settleDataDto.getRetryRecordList());
            //修改结算单
            settleOrderService.batchSettleUpdate(settleOrderList, "");
            //结算特殊处理接口
            settleSpecial(settleOrderList, settleOrderDto);
        } else {
            MchIdHolder.set(settleOrderDto.getMchId());
            //创建交易
            CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto.build(TradeTypeEnum.TRANSFER_REFUND.getCode(), getTradeFundAccountId(settleOrderDto), settleOrderDto.getSettleAmountDto().getTotalAmount(), "", "");
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
            TradeRequestDto tradeRequest = TradeRequestDto.build(createTradeResponseDto.getTradeId(), getTradeFundAccountId(settleOrderDto), getTradeChannel(), settleOrderDto.getTradePassword(), settleDataDto.getFeeItemList());
            TradeResponseDto tradeResponseDto = RpcResultResolver.resolver(payRpc.commitTrade(tradeRequest), ServiceNameHolder.PAY_SERVICE_NAME);

            MchIdHolder.clear();

            //保存流水
            createAccountSerial(settleOrderDto, tradeResponseDto, createTradeResponseDto.getTradeId());
        }
        return settleOrderList;
    }

    @Override
    public SettleDataDto prepareSettleData(List<SettleOrder> settleOrderList, SettleOrderDto settleOrderDto) {
        LocalDateTime localDateTime = DateUtil.nowDateTime();

        SettleDataDto settleDataDto = new SettleDataDto();
        List<RetryRecord> retryRecordList = new ArrayList<>(settleOrderList.size());
        Map<Long, SettleOrder> settleOrderMap = new ConcurrentHashMap<>();
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
                addFeeItem(feeItemList, FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getChargeItemId(), settleFeeItem.getChargeItemName(), String.format("%s|退款，退款单号%s", temp.getBusinessType(), temp.getOrderCode())));
                if (BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(settleFeeItem.getFeeType())) {
                    AccountMergeDto accountMergeDto = earnestMap.get(temp.getCustomerId());
                    accountMergeDto = accountMergeDto == null ? AccountMergeDto.build(temp.getMchId(), temp.getCustomerId()) : accountMergeDto;
                    accountMergeDto.subAmount(settleFeeItem.getAmount(), CustomerAccountSerial.build(settleFeeItem.getFeeType(), ActionEnum.EXPENSE.getCode(), SceneEnum.REFUND.getCode(), settleFeeItem.getAmount(), localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), settleFeeItem.getSettleOrderCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), temp.getOrderCode()));
                    accountMergeDto.subFrozenAmount(settleFeeItem.getAmount());
                    earnestMap.put(temp.getCustomerId(), accountMergeDto);
                }
            }
            if (settleFeeItem.getChargeItemType().equals(ChargeItemTypeEnum.DEDUCT.getCode())) {
                addFeeItem(deductFeeItemList, FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getChargeItemId(), settleFeeItem.getChargeItemName(), String.format("%s|扣减，退款单号%s", temp.getBusinessType(), temp.getOrderCode())));
            }
        }
        //转抵项
        List<TransferDetail> transferDetailList = transferDetailService.listBySettleOrderIdList(settleOrderDto.getIdList());
        for (TransferDetail detail : transferDetailList) {
            SettleOrder temp = settleOrderMap.get(detail.getSettleOrderId());
            addFeeItem(deductFeeItemList, FeeItemDto.build(detail.getAmount(), detail.getChargeItemId(), detail.getChargeItemName(), String.format("%s|转抵，退款单号%s", temp.getBusinessType(), temp.getOrderCode())));
            AccountMergeDto accountMergeDto = transferMap.get(temp.getCustomerId());
            accountMergeDto = accountMergeDto == null ? AccountMergeDto.build(temp.getMchId(), temp.getCustomerId()) : accountMergeDto;
            accountMergeDto.addAmount(detail.getAmount(), CustomerAccountSerial.build(BusinessChargeItemEnum.SystemSubjectType.转抵.getCode(), ActionEnum.INCOME.getCode(), SceneEnum.REFUND_TRANSFER_IN.getCode(), detail.getAmount(), localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), detail.getSettleOrderCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), temp.getOrderCode()));
            transferMap.put(temp.getCustomerId(), accountMergeDto);
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
        settleOrder.setChargeDate(localDateTime.toLocalDate());
    }

    @Override
    public Long getTradeFundAccountId(SettleOrderDto settleOrderDto) {
        return 0L;
    }
}
