package com.dili.settlement.settle.pay;

import com.dili.settlement.component.MchIdHolder;
import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.settlement.domain.RetryRecord;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleDataDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.CreateTradeRequestDto;
import com.dili.settlement.dto.pay.CreateTradeResponseDto;
import com.dili.settlement.dto.pay.FeeItemDto;
import com.dili.settlement.dto.pay.TradeRequestDto;
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
                settleDataDto = prepareSettleData(settleOrderList, settleOrderDto, settleOrderDto.getDeductAmount());
            } else {//不抵扣 结算金额即为应缴总额
                if (!settleOrderDto.getTotalAmount().equals(settleOrderDto.getSettleAmount())) {
                    throw new BusinessException("", "总金额与实际结算金额不相符");
                }
                //构建结算相关信息
                settleDataDto = prepareSettleData(settleOrderList, settleOrderDto);
            }

            //创建交易
            CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto.build(TradeTypeEnum.FEE.getCode(), getTradeFundAccountId(settleOrderDto), settleOrderDto.getTotalAmount(), "", "");
            CreateTradeResponseDto createTradeResponseDto = RpcResultResolver.resolver(payRpc.prepareTrade(createTradeRequestDto), ServiceNameHolder.PAY_SERVICE_NAME);
            //操作定金账户
            customerAccountService.handlePay(settleOrderDto.getMchId(), settleOrderDto.getCustomerId(), settleDataDto.getEarnestAmount(), settleDataDto.getAccountSerialList());
            //存储重试记录
            retryRecordService.batchInsert(settleDataDto.getRetryRecordList());
            //修改结算单
            settleOrderService.batchSettleUpdate(settleOrderList, createTradeResponseDto.getTradeId());
            //结算特殊处理接口
            settleSpecial(settleOrderList, settleOrderDto);
            //提交交易
            TradeRequestDto tradeRequest = TradeRequestDto.build(createTradeResponseDto.getTradeId(), getTradeFundAccountId(settleOrderDto), getTradeChannel(), settleOrderDto.getTradePassword(), settleDataDto.getFeeItemList(), settleDataDto.getDeductFeeItemList());
            RpcResultResolver.resolver(payRpc.commitTrade(tradeRequest), ServiceNameHolder.PAY_SERVICE_NAME);

            MchIdHolder.clear();
        }
        return settleOrderList;
    }

    /**
     * 准备结算数据
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
        for (SettleOrder settleOrder : settleOrderList) {//构建结算单信息以及回调记录列表
            buildSettleInfo(settleOrder, settleOrderDto, localDateTime);
            retryRecordList.add(RetryRecord.build(settleOrder.getId()));
        }
        List<SettleFeeItem> settleFeeItemList = settleFeeItemService.listBySettleOrderIdList(settleOrderDto.getIdList());
        for (SettleFeeItem settleFeeItem : settleFeeItemList) {//计算定金总额、构建流水、支付费用项列表
            feeItemList.add(FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getFeeType(), settleFeeItem.getFeeName()));
            //如果有定金,则计算定金总额以及构建流水
            if (Integer.valueOf(FeeTypeEnum.定金.getCode()).equals(settleFeeItem.getFeeType())) {
                earnestAmount += settleFeeItem.getAmount();
                accountSerialList.add(CustomerAccountSerial.build(ActionEnum.INCOME.getCode(), SceneEnum.PAYMENT.getCode(), settleFeeItem.getAmount(), localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), settleFeeItem.getSettleOrderCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), ""));
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
        for (SettleOrder settleOrder : settleOrderList) {//构建结算单信息以及回调记录列表
            buildSettleInfo(settleOrder, settleOrderDto, localDateTime);
            retryRecordList.add(RetryRecord.build(settleOrder.getId()));
            if (Integer.valueOf(EnableEnum.NO.getCode()).equals(settleOrderDto.getDeductEnable())) {
                continue;
            }
            if (totalDeductAmount == 0L) {//总抵扣额用完时不再处理
                continue;
            }
            //计算抵扣金额
            long deductAmount = settleOrder.getAmount() >= totalDeductAmount ? totalDeductAmount : settleOrder.getAmount();
            totalDeductAmount -= deductAmount;
            settleOrder.setDeductAmount(deductAmount);
            deductFeeItemList.add(FeeItemDto.build(deductAmount, FeeTypeEnum.定金.getCode(), FeeTypeEnum.定金.getName()));
            accountSerialList.add(CustomerAccountSerial.build(ActionEnum.EXPENSE.getCode(), SceneEnum.DEDUCT.getCode(), deductAmount, localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), settleOrder.getCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), ""));
        }
        List<SettleFeeItem> settleFeeItemList = settleFeeItemService.listBySettleOrderIdList(settleOrderDto.getIdList());
        for (SettleFeeItem settleFeeItem : settleFeeItemList) {//计算定金总额、构建流水、支付费用项列表
            feeItemList.add(FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getFeeType(), settleFeeItem.getFeeName()));
            //如果有定金,则计算定金总额以及构建流水
            if (Integer.valueOf(FeeTypeEnum.定金.getCode()).equals(settleFeeItem.getFeeType())) {
                earnestAmount += settleFeeItem.getAmount();
                accountSerialList.add(CustomerAccountSerial.build(ActionEnum.INCOME.getCode(), SceneEnum.PAYMENT.getCode(), settleFeeItem.getAmount(), localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), settleFeeItem.getSettleOrderCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), ""));
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
}
