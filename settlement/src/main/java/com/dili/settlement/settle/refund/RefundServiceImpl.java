package com.dili.settlement.settle.refund;

import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.settlement.component.MchIdHolder;
import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.settlement.domain.RetryRecord;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleDataDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.*;
import com.dili.settlement.enums.ActionEnum;
import com.dili.settlement.enums.RelationTypeEnum;
import com.dili.settlement.enums.SceneEnum;
import com.dili.settlement.enums.TradeTypeEnum;
import com.dili.settlement.handler.ServiceNameHolder;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.service.RetryRecordService;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.settle.RefundService;
import com.dili.settlement.settle.impl.SettleServiceImpl;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.exception.BusinessException;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "refund/special";
    }

    @Override
    public void validParams(SettleOrderDto settleOrderDto) {
        //验证结算金额与总金额是否相等
        if (!settleOrderDto.getTotalAmount().equals(settleOrderDto.getSettleAmount())) {
            throw new BusinessException("", "总金额与实际结算金额不相符");
        }
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
        if (settleOrderDto.getTotalAmount() == 0L) {//结算总额为0则不走支付，也不存在抵扣
            //构建结算相关信息
            SettleDataDto settleDataDto = prepareSettleData(settleOrderList, settleOrderDto);
            //存储重试记录
            retryRecordService.batchInsert(settleDataDto.getRetryRecordList());
            //修改结算单
            settleOrderService.batchSettleUpdate(settleOrderList, "");
            //结算特殊处理接口
            settleSpecial(settleOrderList, settleOrderDto);
        } else {
            MchIdHolder.set(settleOrderDto.getMchId());
            SettleDataDto settleDataDto = prepareSettleData(settleOrderList, settleOrderDto);
            //创建交易
            CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto.build(TradeTypeEnum.TRANSFER_REFUND.getCode(), getTradeFundAccountId(settleOrderDto), settleOrderDto.getTotalAmount(), "", "");
            CreateTradeResponseDto createTradeResponseDto = RpcResultResolver.resolver(payRpc.prepareTrade(createTradeRequestDto), ServiceNameHolder.PAY_SERVICE_NAME);
            //操作定金账户
            customerAccountService.handleRefund(settleOrderDto.getMchId(), settleOrderDto.getCustomerId(), settleDataDto.getEarnestAmount(), settleDataDto.getAccountSerialList());
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
            addFeeItem(feeItemList, FeeItemDto.build(settleFeeItem.getAmount(), settleFeeItem.getChargeItemId(), settleFeeItem.getChargeItemName(), String.format("%s|退款，退款单号%s", settleOrderMap.get(settleFeeItem.getSettleOrderId()).getBusinessType(), settleOrderMap.get(settleFeeItem.getSettleOrderId()).getOrderCode())));
            //如果有定金,则计算定金总额以及构建流水
            if (BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(settleFeeItem.getFeeType())) {
                earnestAmount += settleFeeItem.getAmount();
                accountSerialList.add(CustomerAccountSerial.build(ActionEnum.EXPENSE.getCode(), SceneEnum.REFUND.getCode(), settleFeeItem.getAmount(), localDateTime, settleOrderDto.getOperatorId(), settleOrderDto.getOperatorName(), settleFeeItem.getSettleOrderCode(), RelationTypeEnum.SETTLE_ORDER.getCode(), settleOrderMap.get(settleFeeItem.getSettleOrderId()).getBusinessCode()));
            }
        }
        //设置值
        settleDataDto.setEarnestAmount(0L - earnestAmount);
        settleDataDto.setRetryRecordList(retryRecordList);
        settleDataDto.setFeeItemList(feeItemList);
        settleDataDto.setAccountSerialList(accountSerialList);
        return settleDataDto;
    }

    @Override
    public void buildSettleInfoSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto, LocalDateTime localDateTime) {
        settleOrder.setChargeDate(localDateTime.toLocalDate());
    }

    @Override
    public SettleDataDto prepareSettleData(List<SettleOrder> settleOrderList, SettleOrderDto settleOrderDto, Long totalDeductAmount) {
        return null;
    }

    @Override
    public Long getTradeFundAccountId(SettleOrderDto settleOrderDto) {
        return 0L;
    }
}
