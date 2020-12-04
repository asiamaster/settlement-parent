package com.dili.settlement.settle.pay;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.component.MchIdHolder;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.BalanceResponseDto;
import com.dili.settlement.dto.pay.CreateTradeRequestDto;
import com.dili.settlement.dto.pay.PasswordRequestDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.enums.TradeChannelEnum;
import com.dili.settlement.handler.ServiceNameHolder;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.rpc.AccountQueryRpc;
import com.dili.settlement.rpc.resolver.PayRpcResolver;
import com.dili.settlement.settle.PayService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.time.LocalDateTime;

/**
 * 园区卡
 */
@Service
public class CardPayServiceImpl extends PayServiceImpl implements PayService {

    @Autowired
    private PayRpcResolver payRpcResolver;
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
        MchIdHolder.set(settleOrderDto.getMarketId());
        //校验密码
        PasswordRequestDto passwordRequestDto = PasswordRequestDto.build(settleOrderDto.getTradeFundAccountId(), settleOrderDto.getTradePassword());
        BaseOutput baseOutput = payRpc.validateTradePassword(passwordRequestDto);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException("", "交易密码错误");
        }
        //验证余额
        CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto.build(settleOrderDto.getTradeFundAccountId());
        BalanceResponseDto balanceResponseDto= RpcResultResolver.resolver(payRpc.getAccountBalance(createTradeRequestDto), ServiceNameHolder.PAY_SERVICE_NAME);
        if (settleOrderDto.getSettleAmount() > balanceResponseDto.getAvailableAmount()) {
            throw new BusinessException("", "账户余额不足");
        }
        MchIdHolder.clear();
    }

    @Override
    public void buildSettleInfoSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto, LocalDateTime localDateTime) {
        settleOrder.setTradeCardNo(settleOrderDto.getTradeCardNo());
        settleOrder.setTradeAccountId(settleOrderDto.getTradeAccountId());
        settleOrder.setTradeFundAccountId(settleOrderDto.getTradeFundAccountId());
        settleOrder.setTradeCustomerId(settleOrderDto.getTradeCustomerId());
        settleOrder.setTradeCustomerName(settleOrderDto.getTradeCustomerName());
        settleOrder.setSerialNumber(settleOrderDto.getTradeCardNo() + "(" + settleOrderDto.getTradeCustomerName() + ")");
    }

    @Override
    public Long getTradeFundAccountId(SettleOrderDto settleOrderDto) {
        return settleOrderDto.getTradeFundAccountId();
    }

    @Override
    public Integer getTradeChannel() {
        return TradeChannelEnum.BALANCE.getCode();
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.CARD.getCode();
    }
}
