package com.dili.settlement.settle.order;

import cn.hutool.core.collection.CollUtil;
import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.TransferDetail;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.ChargeItemTypeEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.settle.OrderService;
import com.dili.settlement.settle.impl.OrderServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 退款单相关
 */
@Component
public class RefundOrderServiceImpl extends OrderServiceImpl implements OrderService {

    @Override
    public void createAccountOrFreeze(SettleOrder settleOrder, SettleOrderDto settleOrderDto) {
        //退款费用项里有定金则冻结
        long earnestAmount = countEarnestAmount(settleOrderDto.getSettleFeeItemList());
        customerAccountService.freeze(settleOrder.getMchId(), settleOrder.getCustomerId(), earnestAmount);
        //如果有转抵则为转抵客户创建账户
        for (TransferDetail detail : settleOrderDto.getTransferDetailList()) {
            CustomerAccount customerAccount = CustomerAccount.build(settleOrder.getMarketId(), settleOrder.getMarketCode(), settleOrder.getMchId(), settleOrder.getMchName(), detail.getCustomerId(), detail.getCustomerName(), detail.getCustomerPhone(), detail.getCustomerCertificate());
            customerAccountService.create(customerAccount);
        }
    }

    @Override
    public void saveSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto) {
        if (CollUtil.isNotEmpty(settleOrderDto.getTransferDetailList())) {
            for (TransferDetail detail : settleOrderDto.getTransferDetailList()) {
                detail.setSettleOrderId(settleOrder.getId());
                detail.setSettleOrderCode(settleOrder.getCode());
            }
            transferDetailService.batchInsert(settleOrderDto.getTransferDetailList());
        }
    }

    @Override
    public void cancelSpecial(SettleOrder settleOrder) {
        //费用项中有定金则解冻
        List<SettleFeeItem> itemList = settleFeeItemService.listBySettleOrderId(settleOrder.getId());
        List<SettleFeeItem> settleFeeItemList = itemList.stream().filter(temp -> temp.getChargeItemType().equals(ChargeItemTypeEnum.FEE.getCode())).collect(Collectors.toList());
        long earnestAmount = countEarnestAmount(settleFeeItemList);
        customerAccountService.unfreeze(settleOrder.getMchId(), settleOrder.getCustomerId(), earnestAmount);
        //删除转抵明细
        transferDetailService.deleteBySettleOrderId(settleOrder.getId());
    }

    @Override
    public Integer supportType() {
        return SettleTypeEnum.REFUND.getCode();
    }
}
