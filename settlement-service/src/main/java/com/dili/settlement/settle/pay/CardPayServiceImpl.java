package com.dili.settlement.settle.pay;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.component.MchIdHolder;
import com.dili.settlement.domain.RetryRecord;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.UserAccountCardResponseDto;
import com.dili.settlement.dto.UserAccountSingleQueryDto;
import com.dili.settlement.dto.pay.*;
import com.dili.settlement.enums.RetryTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.enums.TradeChannelEnum;
import com.dili.settlement.enums.TradeTypeEnum;
import com.dili.settlement.rpc.AccountQueryRpc;
import com.dili.settlement.rpc.resolver.GenericRpcResolver;
import com.dili.settlement.rpc.resolver.PayRpcResolver;
import com.dili.settlement.service.ApplicationConfigService;
import com.dili.settlement.settle.PayService;
import com.dili.ss.exception.BusinessException;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

/**
 * 园区卡
 */
@Service
public class CardPayServiceImpl extends PayServiceImpl implements PayService {

    @Autowired
    private PayRpcResolver payRpcResolver;
    @Autowired
    private ApplicationConfigService applicationConfigService;
    @Autowired
    private AccountQueryRpc accountQueryRpc;

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "pay/special_card";
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        if (StrUtil.isBlank(settleOrderDto.getTradeCardNo())) {
            throw new BusinessException("", "交易卡号为空");
        }
        if (settleOrderDto.getTradeFundAccountId() == null) {
            throw new BusinessException("", "交易资金账户ID为空");
        }
        if (settleOrderDto.getTradeAccountId() == null) {
            throw new BusinessException("", "交易账户ID为空");
        }
        if (settleOrderDto.getTradeCustomerId() == null) {
            throw new BusinessException("", "交易客户ID为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getTradeCustomerName())) {
            throw new BusinessException("", "交易客户姓名为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getTradePassword())) {
            throw new BusinessException("", "交易密码为空");
        }
    }

    @Override
    public void settleParamSpecial(SettleOrder po, SettleOrderDto settleOrderDto) {
        po.setTradeCardNo(settleOrderDto.getTradeCardNo());
        po.setTradeAccountId(settleOrderDto.getTradeAccountId());
        po.setTradeFundAccountId(settleOrderDto.getTradeFundAccountId());
        po.setTradeCustomerId(settleOrderDto.getTradeCustomerId());
        po.setTradeCustomerName(settleOrderDto.getTradeCustomerName());
        po.setSerialNumber(settleOrderDto.getTradeCardNo() + "(" + settleOrderDto.getTradeCustomerName() + ")");
    }

    @GlobalTransactional
    @Transactional
    @Override
    public void settle(SettleOrder po, SettleOrderDto settleOrderDto) {
        settleBefore(po, settleOrderDto);
        //调用接口验证园区卡是否可用
        checkCardInfo(settleOrderDto.getTradeCardNo(), po.getMarketId());
        //构建创建交易参数
        MchIdHolder.set(po.getMarketId());//设置市场ID
        if (po.getAmount() != 0L) {
            CreateTradeRequestDto createTradeRequest = CreateTradeRequestDto.build(TradeTypeEnum.FEE.getCode(), settleOrderDto.getTradeFundAccountId(), po.getAmount(), PAY_BUSINESS_PREFIX + po.getOrderCode(), "摊位租赁");
            //创建交易
            CreateTradeResponseDto createTradeResponseDto = payRpcResolver.prePay(createTradeRequest);
            po.setTradeNo(createTradeResponseDto.getTradeId());
        }

        int i = settleOrderService.updateSettle(po);
        if (i != 1) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }

        //存入回调重试记录  方便定时任务扫描
        RetryRecord retryRecord = new RetryRecord(RetryTypeEnum.SETTLE_CALLBACK.getCode(), po.getId(), po.getCode());
        retryRecordService.insertSelective(retryRecord);
        //po.setRetryRecordId(retryRecord.getId());

        //修改市场虚拟资金
        fundAccountService.add(po.getMarketId(), po.getAppId(), po.getAmount());

        if (po.getAmount() != 0L) {
            //提交交易
            TradeRequestDto tradeRequest = TradeRequestDto.build(po.getTradeNo(), settleOrderDto.getTradeFundAccountId(), TradeChannelEnum.BALANCE.getCode(), settleOrderDto.getTradePassword(), createFees(po));
            payRpcResolver.trade(tradeRequest);
        }

        MchIdHolder.clear();//清除市场ID
    }


    @Override
    public void invalidSpecial(SettleOrder po, SettleOrder reverseOrder) {
        fundAccountService.sub(po.getMarketId(), po.getAppId(), po.getAmount());
        MchIdHolder.set(po.getMarketId());//设置市场ID
        InvalidTradeRequestDto requestDto = InvalidTradeRequestDto.build(po.getTradeNo());
        TradeResponseDto responseDto = payRpcResolver.invalid(requestDto);
        MchIdHolder.clear();//清除市场ID
    }

    /**
     * 构建费用
     *
     * @param po
     * @return
     */
    private List<FeeItemDto> createFees(SettleOrder po) {
        List<FeeItemDto> fees = new ArrayList<>();
        //FeeItemDto feeItem = FeeItemDto.build(po.getAmount(), po.getBusinessType(), applicationConfigService.getVal(po.getAppId(), AppGroupCodeEnum.APP_BUSINESS_TYPE.getCode(), po.getBusinessType()));
        //feeItem.setTypeName(applicationConfigService.getVal(po.getAppId(), AppGroupCodeEnum.APP_BUSINESS_TYPE.getCode(), po.getBusinessType()));
        //fees.add(feeItem);
        return fees;
    }

    /**
     * 验证查询卡号
     *
     * @param tradeCardNo
     * @param firmId
     * @return
     */
    private UserAccountCardResponseDto checkCardInfo(String tradeCardNo, Long firmId) {
        UserAccountSingleQueryDto cardQuery = new UserAccountSingleQueryDto();
        cardQuery.setCardNo(tradeCardNo);
        cardQuery.setFirmId(firmId);
        return GenericRpcResolver.resolver(accountQueryRpc.findSingle(cardQuery), "account-service");
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.CARD.getCode();
    }
}
