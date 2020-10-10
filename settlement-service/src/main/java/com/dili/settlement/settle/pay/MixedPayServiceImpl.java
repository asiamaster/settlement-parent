package com.dili.settlement.settle.pay;

import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.SettleWayDetailService;
import com.dili.settlement.settle.PayService;
import com.dili.ss.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 混合支付
 */
@Service
public class MixedPayServiceImpl extends PayServiceImpl implements PayService {

    @Autowired
    private SettleWayDetailService settleWayDetailService;

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
        Long totalAmount = settleOrderService.queryTotalAmount(settleOrderDto);
        if (!Long.valueOf(tempAmount).equals(totalAmount)) {
            throw new BusinessException("", "收款金额与结算金额不相等");
        }
    }

    @Override
    public void settleAfter(SettleOrder po, SettleOrderDto settleOrderDto) {
        fundAccountService.add(po.getMarketId(), po.getAppId(), po.getAmount());
        for (SettleWayDetail temp : settleOrderDto.getSettleWayDetailList()) {
            temp.setOrderId(po.getId());
            temp.setOrderCode(po.getCode());
        }
        settleWayDetailService.batchInsert(settleOrderDto.getSettleWayDetailList());
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.MIXED_PAY.getCode();
    }
}
