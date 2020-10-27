package com.dili.settlement.rpc;

import com.dili.settlement.config.PayRpcFeignConfig;
import com.dili.settlement.dto.pay.*;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @description： 支付服务调用
 *
 * @author ：WangBo
 * @time ：2020年6月22日下午5:52:52
 */
@FeignClient(value = "pay-service", configuration = PayRpcFeignConfig.class)
public interface PayRpc {
    /**
     * 提交交易(充值、提现)
     *
     * @date 2020/7/1
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.trade.service:commit", method = RequestMethod.POST)
    BaseOutput<TradeResponseDto> commitTrade(TradeRequestDto requestDto);

    /**
     * 创建交易（预支付）
     * @date 2020/7/1
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.trade.service:prepare", method = RequestMethod.POST)
    BaseOutput<CreateTradeResponseDto> preparePay(CreateTradeRequestDto createTradeRequest);

    /**
     * 查询余额
     *
     * @author miaoguoxin
     * @date 2020/6/30
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:query", method = RequestMethod.POST)
    BaseOutput<BalanceResponseDto> getAccountBalance(CreateTradeRequestDto requestDto);

    /**
     * 撤销交易
     * @param requestDto
     * @return
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.trade.service:cancel", method = RequestMethod.POST)
    BaseOutput<TradeResponseDto> invalidTrade(InvalidTradeRequestDto requestDto);
}
