package com.dili.settlement.settle.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.settlement.dispatcher.PayDispatcher;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleOrderLink;
import com.dili.settlement.domain.TransferDetail;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.handler.ServiceNameHolder;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.service.*;
import com.dili.settlement.settle.OrderService;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.rpc.FirmRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 结算单相关操作基础实现类
 */
public abstract class OrderServiceImpl implements OrderService {

    @Autowired
    protected SettleOrderService settleOrderService;
    @Autowired
    protected PayDispatcher payDispatcher;
    @Autowired
    protected SettleFeeItemService settleFeeItemService;
    @Autowired
    protected SettleOrderLinkService settleOrderLinkService;
    @Autowired
    protected CustomerAccountService customerAccountService;
    @Autowired
    protected TransferDetailService transferDetailService;
    @Autowired
    protected FirmRpc firmRpc;

    @Transactional
    @Override
    public SettleOrder save(SettleOrderDto settleOrderDto) {
        //查询商户名称并赋值，以便存储
        Firm firm = RpcResultResolver.resolver(firmRpc.getById(settleOrderDto.getMchId()), ServiceNameHolder.UAP_SERVICE_NAME);
        settleOrderDto.setMchName(firm.getName());

        if (settleOrderService.existsOrderCode(settleOrderDto.getAppId(), settleOrderDto.getOrderCode())) {
            throw new BusinessException("", "业务单号已存在");
        }
        SettleOrder settleOrder = BeanUtil.toBean(settleOrderDto, SettleOrder.class);
        //操作客户账户相关  创建、冻结
        createAccountOrFreeze(settleOrder, settleOrderDto);
        //保存结算单
        settleOrderService.insertSelective(settleOrder);
        //保存缴费项列表
        for (SettleFeeItem feeItem : settleOrderDto.getSettleFeeItemList()) {
            feeItem.setSettleOrderId(settleOrder.getId());
            feeItem.setSettleOrderCode(settleOrder.getCode());
        }
        settleFeeItemService.batchInsert(settleOrderDto.getSettleFeeItemList());
        //保存抵扣项列表
        if (CollUtil.isNotEmpty(settleOrderDto.getDeductFeeItemList())) {
            for (SettleFeeItem feeItem : settleOrderDto.getDeductFeeItemList()) {
                feeItem.setSettleOrderId(settleOrder.getId());
                feeItem.setSettleOrderCode(settleOrder.getCode());
            }
            settleFeeItemService.batchInsert(settleOrderDto.getDeductFeeItemList());
        }
        //保存链接列表
        for (SettleOrderLink orderLink : settleOrderDto.getSettleOrderLinkList()) {
            orderLink.setSettleOrderId(settleOrder.getId());
        }
        settleOrderLinkService.batchInsert(settleOrderDto.getSettleOrderLinkList());
        //特殊处理
        saveSpecial(settleOrder, settleOrderDto);
        return settleOrder;
    }

    @Override
    public void createAccountOrFreeze(SettleOrder settleOrder, SettleOrderDto settleOrderDto) {
        return;
    }

    @Override
    public void saveSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto) {
        return;
    }

    @Transactional
    @Override
    public void cancel(SettleOrder settleOrder) {
        if (!settleOrder.getState().equals(SettleStateEnum.WAIT_DEAL.getCode())) {
            throw new BusinessException("", "非待处理的结算单无法取消");
        }
        int i = settleOrderService.deleteById(settleOrder.getId(), settleOrder.getVersion());
        if (i != 1) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
        cancelSpecial(settleOrder);
        settleOrderLinkService.deleteBySettleOrderId(settleOrder.getId());
        settleFeeItemService.deleteBySettleOrderId(settleOrder.getId());
    }

    @Override
    public void cancelSpecial(SettleOrder settleOrder) {
        return;
    }

    /**
     * 计算定金总额
     * @return
     */
    protected long countEarnestAmount(List<SettleFeeItem> settleFeeItemList) {
        return settleFeeItemList.stream().mapToLong(temp -> BusinessChargeItemEnum.SystemSubjectType.定金.getCode().equals(temp.getFeeType()) ? temp.getAmount() : 0L).sum();
    }

    /**
     * 计算转抵扣总额
     * @param settleFeeItemList
     * @return
     */
    protected long countDeductTransferAmount(List<SettleFeeItem> settleFeeItemList) {
        return settleFeeItemList.stream().mapToLong(temp -> BusinessChargeItemEnum.SystemSubjectType.转抵.getCode().equals(temp.getFeeType()) ? temp.getAmount() : 0L).sum();
    }

    /**
     * 计算转抵总额
     * @param transferDetailList
     * @return
     */
    protected long countTransferAmount(List<TransferDetail> transferDetailList) {
        return transferDetailList.stream().mapToLong(TransferDetail::getAmount).sum();
    }
}
