package com.dili.settlement.settle.refund;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.UserAccountCardQuery;
import com.dili.settlement.dto.UserAccountCardResponseDto;
import com.dili.settlement.dto.UserAccountSingleQueryDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.enums.TradeChannelEnum;
import com.dili.settlement.handler.ServiceNameHolder;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.settle.RefundService;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 园区卡
 */
@Service
public class CardRefundServiceImpl extends RefundServiceImpl implements RefundService {

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        UserAccountCardQuery query = new UserAccountCardQuery();
        List<Long> customerIdList = new ArrayList<>(1);
        customerIdList.add(settleOrderDto.getCustomerId());
        query.setCustomerIds(customerIdList);
        query.setFirmId(settleOrderDto.getMarketId());
        modelMap.addAttribute("cardList", RpcResultResolver.resolver(accountQueryRpc.getList(query), ServiceNameHolder.ACCOUNT_SERVICE_NAME));
        return "refund/special_card";
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getTradeAccountId() == null) {
            throw new BusinessException("", "交易账户ID为空");
        }
        UserAccountCardResponseDto cardResponseDto = checkCardInfo(settleOrderDto.getTradeAccountId(), settleOrderDto.getMarketId());
        settleOrderDto.setTradeCardNo(cardResponseDto.getCardNo());
        settleOrderDto.setTradeAccountId(cardResponseDto.getAccountId());
        settleOrderDto.setTradeFundAccountId(cardResponseDto.getFundAccountId());
        settleOrderDto.setTradeCustomerId(cardResponseDto.getCustomerId());
        settleOrderDto.setTradeCustomerName(cardResponseDto.getCustomerName());
        settleOrderDto.setTradeCustomerCode(cardResponseDto.getCustomerCode());
        settleOrderDto.setHoldName(cardResponseDto.getHoldName());
        settleOrderDto.setHoldCertificateNumber(cardResponseDto.getHoldCertificateNumber());
        settleOrderDto.setHoldContactsPhone(cardResponseDto.getHoldContactsPhone());
    }

    /**
     * 验证卡账户信息
     * @param tradeAccountId
     * @param marketId
     * @return
     */
    private UserAccountCardResponseDto checkCardInfo(Long tradeAccountId, Long marketId) {
        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setAccountId(tradeAccountId);
        query.setFirmId(marketId);
        return RpcResultResolver.resolver(accountQueryRpc.findSingle(query), ServiceNameHolder.ACCOUNT_SERVICE_NAME);
    }

    @Override
    public void buildSettleInfoSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto, LocalDateTime localDateTime) {
        settleOrder.setTradeCardNo(settleOrderDto.getTradeCardNo());
        settleOrder.setTradeAccountId(settleOrderDto.getTradeAccountId());
        settleOrder.setTradeFundAccountId(settleOrderDto.getTradeFundAccountId());
        settleOrder.setTradeCustomerId(settleOrderDto.getTradeCustomerId());
        settleOrder.setTradeCustomerName(settleOrderDto.getTradeCustomerName());
        settleOrder.setSerialNumber(settleOrderDto.getTradeCardNo() + "(" + settleOrderDto.getTradeCustomerName() + ")");
        settleOrder.setHoldName(settleOrderDto.getHoldName());
        settleOrder.setHoldCertificateNumber(settleOrderDto.getHoldCertificateNumber());
        settleOrder.setHoldContactsPhone(settleOrderDto.getHoldContactsPhone());
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
