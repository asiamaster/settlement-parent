package com.dili.settlement.settle.pay;

import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.TradeResponseDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.enums.TradeChannelEnum;
import com.dili.settlement.service.SettleWayDetailService;
import com.dili.settlement.service.SettleWayService;
import com.dili.settlement.settle.PayService;
import com.dili.ss.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

/**
 * 混合支付
 */
@Service
public class MixedPayServiceImpl extends PayServiceImpl implements PayService {

    @Autowired
    private SettleWayDetailService settleWayDetailService;

    @Autowired
    private SettleWayService settleWayService;

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        List<SettleConfig> wayList = settleWayService.payFormList(settleOrderDto.getMarketId());
        modelMap.addAttribute("wayList", wayList);
        return "pay/special_mixed";
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getIdList().size() > 1) {
            throw new BusinessException("", "组合结算方式仅支持单条记录");
        }
        if (CollUtil.isEmpty(settleOrderDto.getSettleWayDetailList())) {
            throw new BusinessException("", "组合结算方式列表为空");
        }
        long tempAmount = 0;
        for (SettleWayDetail detail : settleOrderDto.getSettleWayDetailList()) {
            if (detail.getAmount() == null || detail.getAmount() < 0) {
                throw new BusinessException("", "组合结算方式金额错误");
            }
            tempAmount += detail.getAmount();
        }
        if (!Long.valueOf(tempAmount).equals(settleOrderDto.getSettleAmountDto().countPaySettleAmount())) {
            throw new BusinessException("", "收款金额与实付金额不相等");
        }
    }

    @Override
    public Integer getTradeChannel() {
        return TradeChannelEnum.MIXED.getCode();
    }

    @Override
    public void createAccountSerial(SettleOrderDto settleOrderDto, TradeResponseDto tradeResponse, String tradeId) {
        return;
    }

    @Override
    public void settleSpecial(List<SettleOrder> settleOrderList, SettleOrderDto settleOrderDto) {
        List<SettleWayDetail> settleWayDetailList = new ArrayList<>();
        for (SettleOrder settleOrder : settleOrderList) {
            for (SettleWayDetail temp : settleOrderDto.getSettleWayDetailList()) {
                settleWayDetailList.add(SettleWayDetail.build(settleOrder.getId(), settleOrder.getCode(), temp));
            }
        }
        settleWayDetailService.batchInsert(settleWayDetailList);
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.MIXED_PAY.getCode();
    }
}
