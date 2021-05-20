package com.dili.settlement.settle.order;

import cn.hutool.core.collection.CollUtil;
import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.ChargeItemTypeEnum;
import com.dili.settlement.enums.EnableEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.settle.OrderService;
import com.dili.settlement.settle.impl.OrderServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 缴费单相关
 */
@Component
public class PayOrderServiceImpl extends OrderServiceImpl implements OrderService {

    @Override
    public void createAccountOrFreeze(SettleOrder settleOrder, SettleOrderDto settleOrderDto) {
        //如果费用里包括定金则创建账户
        boolean ifCreate = settleOrderDto.getSettleFeeItemList().stream().anyMatch(temp -> BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(temp.getFeeType()));
        if (ifCreate) {
            CustomerAccount customerAccount = CustomerAccount.build(settleOrder.getMarketId(), settleOrder.getMarketCode(), settleOrder.getMchId(), settleOrder.getMchName(), settleOrder.getCustomerId(), settleOrder.getCustomerName(), settleOrder.getCustomerPhone(), settleOrder.getCustomerCertificate());
            customerAccountService.create(customerAccount);
        }
        //抵扣项中有定金或者转抵则冻结相应账户金额
        long earnestAmount = countEarnestAmount(settleOrderDto.getDeductFeeItemList());
        long transferAmount = countDeductTransferAmount(settleOrderDto.getDeductFeeItemList());
        customerAccountService.freeze(settleOrder.getMchId(), settleOrder.getCustomerId(), earnestAmount);
        customerAccountService.freezeTransfer(settleOrder.getMchId(), settleOrder.getCustomerId(), transferAmount);
    }

    @Override
    public void saveSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto) {
        return;
    }

    @Override
    public void cancelSpecial(SettleOrder settleOrder) {
        //抵扣项中有定金 或者 转抵则解冻相应账户金额
        List<SettleFeeItem> itemList = settleFeeItemService.listBySettleOrderId(settleOrder.getId());
        List<SettleFeeItem> deductFeeItemList = itemList.stream().filter(temp -> temp.getChargeItemType().equals(ChargeItemTypeEnum.DEDUCT.getCode())).collect(Collectors.toList());
        long earnestAmount = countEarnestAmount(deductFeeItemList);
        long transferAmount = countDeductTransferAmount(deductFeeItemList);
        customerAccountService.unfreeze(settleOrder.getMchId(), settleOrder.getCustomerId(), earnestAmount);
        customerAccountService.unfreezeTransfer(settleOrder.getMchId(), settleOrder.getCustomerId(), transferAmount);
    }

    @Override
    public Integer supportType() {
        return SettleTypeEnum.PAY.getCode();
    }
}
