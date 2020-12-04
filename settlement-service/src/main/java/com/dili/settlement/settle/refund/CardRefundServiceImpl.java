package com.dili.settlement.settle.refund;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.enums.TradeChannelEnum;
import com.dili.settlement.settle.RefundService;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.time.LocalDateTime;

/**
 * 园区卡
 */
@Service
public class CardRefundServiceImpl extends RefundServiceImpl implements RefundService {

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "refund/special_card";
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
