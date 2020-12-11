package com.dili.settlement.settle.order;

import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.FeeTypeEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.settle.OrderService;
import com.dili.settlement.settle.impl.OrderServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 退款单相关
 */
@Component
public class RefundOrderServiceImpl extends OrderServiceImpl implements OrderService {

    @Override
    public void saveSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto) {
        long earnestAmount = 0L;
        for (SettleFeeItem item : settleOrderDto.getSettleFeeItemList()) {
            if (!Integer.valueOf(FeeTypeEnum.定金.getCode()).equals(item.getFeeType())) {
                continue;
            }
            earnestAmount += item.getAmount();
        }
        customerAccountService.freeze(settleOrder.getMchId(), settleOrder.getCustomerId(), earnestAmount);
    }

    @Override
    public void cancelSpecial(SettleOrder settleOrder) {
        List<SettleFeeItem> settleFeeItemList = settleFeeItemService.listBySettleOrderId(settleOrder.getId());
        long earnestAmount = 0L;
        for (SettleFeeItem item : settleFeeItemList) {
            if (!Integer.valueOf(FeeTypeEnum.定金.getCode()).equals(item.getFeeType())) {
                continue;
            }
            earnestAmount += item.getAmount();
        }
        customerAccountService.unfreeze(settleOrder.getMchId(), settleOrder.getCustomerId(), earnestAmount);
    }

    @Override
    public Integer supportType() {
        return SettleTypeEnum.REFUND.getCode();
    }
}
