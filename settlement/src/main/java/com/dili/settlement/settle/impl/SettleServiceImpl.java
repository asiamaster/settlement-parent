package com.dili.settlement.settle.impl;

import cn.hutool.core.bean.BeanUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.pay.FeeItemDto;
import com.dili.settlement.enums.ReverseEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.handler.ServiceNameHolder;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.rpc.AccountQueryRpc;
import com.dili.settlement.rpc.PayRpc;
import com.dili.settlement.service.CustomerAccountService;
import com.dili.settlement.service.RetryRecordService;
import com.dili.settlement.service.SettleFeeItemService;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.settle.SettleService;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.exception.BusinessException;
import com.dili.uid.sdk.rpc.feign.UidFeignRpc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 结算验证基础实现类
 */
public abstract class SettleServiceImpl implements SettleService {

    @Autowired
    protected SettleOrderService settleOrderService;

    @Autowired
    protected CustomerAccountService customerAccountService;

    @Autowired
    protected RetryRecordService retryRecordService;

    @Autowired
    protected SettleFeeItemService settleFeeItemService;

    @Autowired
    protected PayRpc payRpc;

    @Autowired
    protected UidFeignRpc uidFeignRpc;

    @Autowired
    protected AccountQueryRpc accountQueryRpc;

    @Override
    public List<SettleOrder> canSettle(List<Long> ids) {
        //查询并锁定
        List<SettleOrder> settleOrderList = settleOrderService.lockList(ids);
        if (ids.size() != settleOrderList.size()) {
            throw new BusinessException("", "结算单记录已变更,请刷新重试");
        }
        if (!settleOrderList.stream().allMatch(temp -> temp.getState().equals(SettleStateEnum.WAIT_DEAL.getCode()))) {
            throw new BusinessException("", "结算单状态已变更,请刷新重试");
        }
        return settleOrderList;
    }

    @Override
    public void buildSettleInfo(SettleOrder settleOrder, SettleOrderDto settleOrderDto, LocalDateTime localDateTime) {
        settleOrder.setDeductAmount(0L);
        settleOrder.setState(SettleStateEnum.DEAL.getCode());
        settleOrder.setWay(settleOrderDto.getWay());
        settleOrder.setOperatorId(settleOrderDto.getOperatorId());
        settleOrder.setOperatorName(settleOrderDto.getOperatorName());
        settleOrder.setOperateTime(localDateTime);
        settleOrder.setNotes(settleOrderDto.getNotes());
        buildSettleInfoSpecial(settleOrder, settleOrderDto, localDateTime);
    }

    @Override
    public void buildSettleInfoSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto, LocalDateTime localDateTime) {
        return;
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        return;
    }

    @Override
    public void settleSpecial(List<SettleOrder> settleOrderList, SettleOrderDto settleOrderDto) {
        return;
    }

    @Override
    public void validParams(SettleOrderDto settleOrderDto) {
        validParamsSpecial(settleOrderDto);
    }

    @Override
    public void invalid(SettleOrder po, InvalidRequestDto param) {
        SettleOrder reverseOrder = new SettleOrder();
        BeanUtil.copyProperties(po, reverseOrder);
        reverseOrder.setId(null);
        reverseOrder.setCode(RpcResultResolver.resolver(uidFeignRpc.getBizNumber(param.getMarketCode() + "_settleOrder"), ServiceNameHolder.UID_SERVICE_NAME));
        reverseOrder.setOrderCode(po.getCode());
        reverseOrder.setOperatorId(param.getOperatorId());
        reverseOrder.setOperatorName(param.getOperatorName());
        reverseOrder.setOperateTime(DateUtil.nowDateTime());
        reverseOrder.setReverse(ReverseEnum.YES.getCode());
        settleOrderService.insertSelective(reverseOrder);

        invalidSpecial(po, reverseOrder, param);
    }

    @Override
    public void invalidSpecial(SettleOrder po, SettleOrder reverseOrder, InvalidRequestDto param) {
        return;
    }

    /**
     * 用于处理支付费用项添加 为0则不添加
     * @param feeItemList
     * @param item
     */
    protected void addFeeItem(List<FeeItemDto> feeItemList, FeeItemDto item) {
        if (item.getAmount() == 0L) {
            return;
        }
        feeItemList.add(item);
    }
}
