package com.dili.settlement.settle.pay;

import cn.hutool.core.bean.BeanUtil;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.settlement.component.MchIdHolder;
import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.settlement.domain.RetryRecord;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
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
import com.dili.ss.exception.BusinessException;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        if (settleOrderDto.getTotalAmount() == 0L) {//结算总额为0则不走支付，也不存在抵扣
            //构建结算相关信息
            SettleDataDto settleDataDto = prepareSettleData(settleOrderList, settleOrderDto);
            //存储重试记录
            retryRecordService.batchInsert(settleDataDto.getRetryRecordList());
            settleOrderService.batchSettleUpdate(settleOrderList, "");
            //结算特殊处理接口
            settleSpecial(settleOrderList, settleOrderDto);
        } else {
            MchIdHolder.set(settleOrderDto.getMchId());
            boolean deduct = Integer.valueOf(EnableEnum.YES.getCode()).equals(settleOrderDto.getDeductEnable());
            SettleDataDto settleDataDto = null;
            if (deduct) {//抵扣
                if (settleOrderDto.getCustomerId() == null) {
                    throw new BusinessException("", "客户ID为空");
                }
                if (settleOrderDto.getTotalDeductAmount() == null || settleOrderDto.getTotalDeductAmount() < 0L) {
                    throw new BusinessException("", "抵扣金额不合法");
                }
                if (settleOrderDto.getTotalAmount() - settleOrderDto.getTotalDeductAmount() != settleOrderDto.getSettleAmount()) {
                    throw new BusinessException("", "总金额与抵扣金额之差与实际结算金额不相符");
                }
                //构建结算相关信息
                settleDataDto = prepareSettleData(settleOrderList, settleOrderDto, settleOrderDto.getTotalDeductAmount());
            } else {//不抵扣 结算金额即为应缴总额
                if (!settleOrderDto.getTotalAmount().equals(settleOrderDto.getSettleAmount())) {
                    throw new BusinessException("", "总金额与实际结算金额不相符");
                }
                //构建结算相关信息
                settleDataDto = prepareSettleData(settleOrderList, settleOrderDto);
            }

            //创建交易
            CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto.build(TradeTypeEnum.SYNTHESIZE.getCode(), getTradeFundAccountId(settleOrderDto), settleOrderDto.getTotalAmount(), "", "");
            CreateTradeResponseDto createTradeResponseDto = RpcResultResolver.resolver(payRpc.prepareTrade(createTradeRequestDto), ServiceNameHolder.PAY_SERVICE_NAME);
            //操作定金账户
            customerAccountService.handle(settleOrderDto.getMchId(), settleOrderDto.getCustomerId(), settleDataDto.getEarnestAmount(), settleDataDto.getAccountSerialList());
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
        SettleDataDto settleDataDto = new SettleDataDto();

        LocalDateTime localDateTime = DateUtil.nowDateTime();
        long earnestAmount = 0L;
        List<RetryRecord> retryRecordList = new ArrayList<>(settleOrderList.size());
        List<FeeItemDto> feeItemList = new ArrayList<>();
        List<CustomerAccountSerial> accountSerialList = new ArrayList<>();
        Map<Long, SettleOrder> settleOrderMap = new ConcurrentHashMap<>();
        for (SettleOrder settleOrder : settleOrderList) {//构建结算单信息以及回调记录列表
            buildSettleInfo(settleOrder, settleOrderDto, localDateTime);
            retryRecordList.add(RetryRecord.build(settleOrder.getId()));
            settleOrderMap.put(settleOrder.getId(), settleOrder);
        }
        List<SettleFeeItem> settleFeeItemList = settleFeeItemService.listBySettleOrderIdList(settleOrderDto.getIdList());
        for (SettleFeeItem settleFeeItem : settleFeeItemList) {//计算定金总额、构建流水、支付费用项列表
            if (settleFeeItem.getAmount() == 0L) {//金额为0不提交到支付、不构建流水、不累计定金
                continue;
            }
            addFeeItem(feeItemList, FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getChargeItemId(), settleFeeItem.getChargeItemName(), String.format("%s|%s单号%s", settleOrderMap.get(settleFeeItem.getSettleOrderId()).getBusinessType(), BizTypeEnum.getNameByCode(settleOrderMap.get(settleFeeItem.getSettleOrderId()).getBusinessType()), settleOrderMap.get(settleFeeItem.getSettleOrderId()).getBusinessCode())));
            //如果有定金,则计算定金总额以及构建流水
            if (BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(settleFeeItem.getFeeType())) {
                earnestAmount += settleFeeItem.getAmount();
                accountSerialList.add(CustomerAccountSerial.build(ActionEnum.INCOME.getCode(), SceneEnum.PAYMENT.getCode(), settleFeeItem.getAmount(), localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), settleFeeItem.getSettleOrderCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), settleOrderMap.get(settleFeeItem.getSettleOrderId()).getBusinessCode()));
            }
        }
        //设置值
        settleDataDto.setEarnestAmount(earnestAmount);
        settleDataDto.setRetryRecordList(retryRecordList);
        settleDataDto.setFeeItemList(feeItemList);
        settleDataDto.setAccountSerialList(accountSerialList);
        return settleDataDto;
    }

    /**
     * 准备结算数据
     *
     * @param settleOrderList
     * @param settleOrderDto
     * @return
     */
    @Override
    public SettleDataDto prepareSettleData(List<SettleOrder> settleOrderList, SettleOrderDto settleOrderDto, Long totalDeductAmount) {
        SettleDataDto settleDataDto = new SettleDataDto();

        LocalDateTime localDateTime = DateUtil.nowDateTime();
        long earnestAmount = 0L - totalDeductAmount;
        List<RetryRecord> retryRecordList = new ArrayList<>(settleOrderList.size());
        List<FeeItemDto> feeItemList = new ArrayList<>();
        List<FeeItemDto> deductFeeItemList = new ArrayList<>();
        List<CustomerAccountSerial> accountSerialList = new ArrayList<>();
        Map<Long, SettleOrder> settleOrderMap = new ConcurrentHashMap<>();
        for (SettleOrder settleOrder : settleOrderList) {//构建结算单信息以及回调记录列表
            buildSettleInfo(settleOrder, settleOrderDto, localDateTime);
            retryRecordList.add(RetryRecord.build(settleOrder.getId()));
            settleOrderMap.put(settleOrder.getId(), settleOrder);
            if (Integer.valueOf(EnableEnum.NO.getCode()).equals(settleOrder.getDeductEnable())) {
                continue;
            }
            if (settleOrder.getAmount() == 0L) {//金额为0不提交到支付、不构建流水、不累计定金
                continue;
            }
            if (totalDeductAmount == 0L) {//总抵扣额用完时不再处理
                continue;
            }
            //计算抵扣金额
            long deductAmount = settleOrder.getAmount() >= totalDeductAmount ? totalDeductAmount : settleOrder.getAmount();
            totalDeductAmount -= deductAmount;
            settleOrder.setDeductAmount(deductAmount);
            BusinessChargeItemDto businessChargeItem = findOneChargeItem(settleOrder.getMarketId(), BizTypeEnum.EARNEST.getCode(), EARNEST_CHARGE_ITEM_CODE);
            if (businessChargeItem == null) {
                throw new BusinessException("", "未查询到定金费用项");
            }
            addFeeItem(deductFeeItemList, FeeItemDto.build(deductAmount, businessChargeItem.getId(), businessChargeItem.getChargeItem(), String.format("%s|定金抵扣充值，关联%s单号%s", settleOrder.getBusinessType(), BizTypeEnum.getNameByCode(settleOrder.getBusinessType()), settleOrder.getBusinessCode())));
            accountSerialList.add(CustomerAccountSerial.build(ActionEnum.EXPENSE.getCode(), SceneEnum.DEDUCT.getCode(), deductAmount, localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), settleOrder.getCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), settleOrder.getBusinessCode()));
        }
        List<SettleFeeItem> settleFeeItemList = settleFeeItemService.listBySettleOrderIdList(settleOrderDto.getIdList());
        for (SettleFeeItem settleFeeItem : settleFeeItemList) {//计算定金总额、构建流水、支付费用项列表
            if (settleFeeItem.getAmount() == 0L) {
                continue;
            }
            addFeeItem(feeItemList, FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getChargeItemId(), settleFeeItem.getChargeItemName(), String.format("%s|%s单号%s", settleOrderMap.get(settleFeeItem.getSettleOrderId()).getBusinessType(), BizTypeEnum.getNameByCode(settleOrderMap.get(settleFeeItem.getSettleOrderId()).getBusinessType()), settleOrderMap.get(settleFeeItem.getSettleOrderId()).getBusinessCode())));
            //如果有定金,则计算定金总额以及构建流水
            if (BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(settleFeeItem.getFeeType())) {
                earnestAmount += settleFeeItem.getAmount();
                accountSerialList.add(CustomerAccountSerial.build(ActionEnum.INCOME.getCode(), SceneEnum.PAYMENT.getCode(), settleFeeItem.getAmount(), localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), settleFeeItem.getSettleOrderCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), settleOrderMap.get(settleFeeItem.getSettleOrderId()).getBusinessCode()));
            }
        }
        //设置值
        settleDataDto.setEarnestAmount(earnestAmount);
        settleDataDto.setRetryRecordList(retryRecordList);
        settleDataDto.setFeeItemList(feeItemList);
        settleDataDto.setDeductFeeItemList(deductFeeItemList);
        settleDataDto.setAccountSerialList(accountSerialList);
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
        long earnestAmount = 0L;
        List<FeeItemDto> feeItemList = new ArrayList<>();
        List<FeeItemDto> deductFeeItemList = new ArrayList<>();
        List<CustomerAccountSerial> accountSerialList = new ArrayList<>();
        if (po.getDeductAmount() != null && po.getDeductAmount() > 0L) {
            earnestAmount = po.getDeductAmount();
            BusinessChargeItemDto businessChargeItem = findOneChargeItem(po.getMarketId(), BizTypeEnum.EARNEST.getCode(), EARNEST_CHARGE_ITEM_CODE);
            if (businessChargeItem == null) {
                throw new BusinessException("", "未查询到定金费用项");
            }
            addFeeItem(deductFeeItemList, FeeItemDto.build(po.getDeductAmount(), businessChargeItem.getId(), businessChargeItem.getChargeItem(), String.format("%s|作废，%s单号%s", po.getBusinessType(), BizTypeEnum.getNameByCode(po.getBusinessType()), po.getBusinessCode())));
            accountSerialList.add(CustomerAccountSerial.build(ActionEnum.INCOME.getCode(), SceneEnum.INVALID_IN.getCode(), po.getDeductAmount(), reverseOrder.getOperateTime(), reverseOrder.getOperatorId(), reverseOrder.getOperatorName(), reverseOrder.getCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), po.getBusinessCode()));
        }
        List<SettleFeeItem> settleFeeItemList = settleFeeItemService.listBySettleOrderId(po.getId());
        for (SettleFeeItem settleFeeItem : settleFeeItemList) {//构建流水、退款费用项列表
            if (settleFeeItem.getAmount() == 0L) {//金额为0不提交到支付、不构建流水、不累计定金
                continue;
            }
            addFeeItem(feeItemList, FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getChargeItemId(), settleFeeItem.getChargeItemName(), String.format("%s|作废，%s单号%s", po.getBusinessType(), BizTypeEnum.getNameByCode(po.getBusinessType()), po.getBusinessCode())));
            //如果有定金,则计算定金总额以及构建流水
            if (BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(settleFeeItem.getFeeType())) {
                earnestAmount -= settleFeeItem.getAmount();
                accountSerialList.add(CustomerAccountSerial.build(ActionEnum.EXPENSE.getCode(), SceneEnum.INVALID_OUT.getCode(), settleFeeItem.getAmount(), reverseOrder.getOperateTime(), reverseOrder.getOperatorId(), reverseOrder.getOperatorName(), reverseOrder.getCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), po.getBusinessCode()));
            }
        }
        //操作客户定金账户
        customerAccountService.handle(po.getMchId(), po.getCustomerId(), earnestAmount, accountSerialList);

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
    }
}
