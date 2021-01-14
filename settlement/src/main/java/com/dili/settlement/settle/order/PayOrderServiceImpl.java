package com.dili.settlement.settle.order;

import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.EnableEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.settle.OrderService;
import com.dili.settlement.settle.impl.OrderServiceImpl;
import org.springframework.stereotype.Component;

/**
 * 缴费单相关
 */
@Component
public class PayOrderServiceImpl extends OrderServiceImpl implements OrderService {

    @Override
    public void saveSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto) {
        //如果费用里包括定金 或者 标记为可抵扣则创建定金账户
        boolean containsEarnestItem = settleOrderDto.getSettleFeeItemList().stream().anyMatch(temp -> BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(temp.getFeeType()));
        if (containsEarnestItem || Integer.valueOf(EnableEnum.YES.getCode()).equals(settleOrder.getDeductEnable())) {
            CustomerAccount customerAccount = CustomerAccount.build(settleOrder.getMarketId(), settleOrder.getMarketCode(), settleOrder.getMchId(), settleOrder.getMchName(), settleOrder.getCustomerId(), settleOrder.getCustomerName(), settleOrder.getCustomerPhone(), settleOrder.getCustomerCertificate());
            customerAccountService.create(customerAccount);
        }
    }

    @Override
    public Integer supportType() {
        return SettleTypeEnum.PAY.getCode();
    }
}
