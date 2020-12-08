package com.dili.settlement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.dispatcher.PayDispatcher;
import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleOrderLink;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleAmountDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.*;
import com.dili.settlement.mapper.SettleOrderMapper;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.service.*;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.rpc.FirmRpc;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-05 16:39:11.
 */
@Service
public class SettleOrderServiceImpl extends BaseServiceImpl<SettleOrder, Long> implements SettleOrderService {
    @Autowired
    private PayDispatcher payDispatcher;
    @Autowired
    private SettleFeeItemService settleFeeItemService;
    @Autowired
    private SettleOrderLinkService settleOrderLinkService;
    @Autowired
    private CustomerAccountService customerAccountService;
    @Autowired
    private FirmRpc firmRpc;

    public SettleOrderMapper getActualDao() {
        return (SettleOrderMapper)getDao();
    }

    @Transactional
    @Override
    public SettleOrder save(SettleOrderDto settleOrderDto) {
        //查询商户名称并赋值，以便存储
        Firm firm = RpcResultResolver.resolver(firmRpc.getById(settleOrderDto.getMchId()), "uap-service");
        settleOrderDto.setMchName(firm.getName());

        if (existsOrderCode(settleOrderDto.getAppId(), settleOrderDto.getOrderCode())) {
            throw new BusinessException("", "业务单号已存在");
        }
        SettleOrder settleOrder = BeanUtil.toBean(settleOrderDto, SettleOrder.class);
        insertSelective(settleOrder);
        //保存缴费项列表
        for (SettleFeeItem feeItem : settleOrderDto.getSettleFeeItemList()) {
            feeItem.setSettleOrderId(settleOrder.getId());
            feeItem.setSettleOrderCode(settleOrder.getCode());
        }
        settleFeeItemService.batchInsert(settleOrderDto.getSettleFeeItemList());
        //保存链接列表
        for (SettleOrderLink orderLink : settleOrderDto.getSettleOrderLinkList()) {
            orderLink.setSettleOrderId(settleOrder.getId());
        }
        settleOrderLinkService.batchInsert(settleOrderDto.getSettleOrderLinkList());
        //如果费用里包括定金 或者 标记为可抵扣则创建定金账户
        boolean containsEarnestItem = settleOrderDto.getSettleFeeItemList().stream().anyMatch(temp -> Integer.valueOf(FeeTypeEnum.定金.getCode()).equals(temp.getFeeType()));
        if (containsEarnestItem || Integer.valueOf(EnableEnum.YES.getCode()).equals(settleOrder.getDeductEnable())) {
            CustomerAccount customerAccount = CustomerAccount.build(settleOrder.getMarketId(), settleOrder.getMarketCode(), settleOrder.getMchId(), settleOrder.getMchName(), settleOrder.getCustomerId(), settleOrder.getCustomerName(), settleOrder.getCustomerPhone(), settleOrder.getCustomerCertificate());
            customerAccountService.create(customerAccount);
        }
        return settleOrder;
    }

    @Override
    public boolean existsOrderCode(Long appId, String orderCode) {
        SettleOrderDto query = new SettleOrderDto();
        query.setAppId(appId);
        query.setOrderCode(orderCode);
        List<SettleOrder> itemList = getActualDao().list(query);
        return !CollUtil.isEmpty(itemList);
    }

    @Override
    public List<SettleOrder> list(SettleOrderDto query) {
        return getActualDao().list(query);
    }

    @Override
    public PageOutput<List<SettleOrder>> listPagination(SettleOrderDto query) {
        PageHelper.startPage(query.getPage(), query.getRows());
        List<SettleOrder> itemList = getActualDao().list(query);

        Page<SettleOrder> page = (Page)itemList;
        PageOutput<List<SettleOrder>> output = PageOutput.success();
        output.setData(itemList);
        output.setPageNum(page.getPageNum());
        output.setPageSize(page.getPageSize());
        output.setTotal(page.getTotal());
        output.setStartRow(page.getStartRow());
        output.setEndRow(page.getEndRow());
        output.setPages(page.getPages());
        return output;
    }

    @Override
    public SettleAmountDto queryAmount(SettleOrderDto settleOrderDto) {
        return getActualDao().queryAmount(settleOrderDto);
    }

    @Transactional
    @Override
    public int updateSettle(SettleOrder po) {
        return getActualDao().settleUpdate(po);
    }

    @Override
    public SettleOrder getByCode(String code) {
        return getActualDao().getByCode(code);
    }

    @Override
    public Long convertReverseOrderId(Long id) {
        SettleOrder reverseOrder = get(id);
        if (reverseOrder == null) {
            throw new BusinessException("", "冲正单记录不存在");
        }
        SettleOrder originOrder = getByCode(reverseOrder.getOrderCode());
        if (originOrder == null) {
            throw new BusinessException("", "结算单记录不存在");
        }
        return originOrder.getId();
    }

    @Override
    public List<SettleOrder> lockList(List<Long> ids) {
        return getActualDao().lockList(ids);
    }

    @Transactional
    @Override
    public int batchSettleUpdate(List<SettleOrder> settleOrderList, String tradeNo) {
        if (CollUtil.isEmpty(settleOrderList)) {
            return 0;
        }
        return getActualDao().batchSettleUpdate(settleOrderList, tradeNo);
    }

    @Override
    public SettleOrder get(Long appId, String orderCode) {
        SettleOrderDto query = new SettleOrderDto();
        query.setAppId(appId);
        query.setOrderCode(orderCode);
        return listByExample(query).stream().findFirst().orElse(null);
    }

    @Transactional
    @Override
    public void cancelById(Long id) {
        SettleOrder po = get(id);
        doCancel(po);
    }

    /**
     * 执行取消
     * @param po
     */
    private void doCancel(SettleOrder po) {
        if (po == null) {
            throw new BusinessException("", "未查询到结算单记录");
        }
        if (!po.getState().equals(SettleStateEnum.WAIT_DEAL.getCode())) {
            throw new BusinessException("", "非待处理的结算单无法取消");
        }
        int i = getActualDao().delWithVersion(po);
        if (i != 1) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
        settleOrderLinkService.deleteBySettleOrderId(po.getId());
        settleFeeItemService.deleteBySettleOrderId(po.getId());
    }

    @Transactional
    @Override
    public void cancelByCode(String code) {
        SettleOrder po = getActualDao().getByCode(code);
        doCancel(po);
    }

    @Transactional
    @Override
    public void cancel(Long appId, String orderCode) {
        SettleOrderDto query = new SettleOrderDto();
        query.setAppId(appId);
        query.setOrderCode(orderCode);
        List<SettleOrder> itemList = getActualDao().list(query);
        if (CollUtil.isEmpty(itemList)) {
            throw new BusinessException("", "未查询到结算单记录");
        }
        SettleOrder po = itemList.get(0);
        doCancel(po);
    }

    /**
     * 目前根据需求，只针对交款单作废
     * @param param
     */
    @GlobalTransactional
    @Transactional
    @Override
    public void invalid(InvalidRequestDto param) {
        for (String orderCode : param.getOrderCodeList()) {
            SettleOrder po = get(param.getAppId(), orderCode);
            if (po == null) {//未查询到
                continue;
            }
            if (!Integer.valueOf(ReverseEnum.NO.getCode()).equals(po.getReverse())) {//冲正单不允许作废
                continue;
            }
            if (Integer.valueOf(SettleTypeEnum.PAY.getCode()).equals(po.getType())) {//非支付单不允许作废
                if (Integer.valueOf(SettleStateEnum.WAIT_DEAL.getCode()).equals(po.getState())) {//待处理直接删除
                    doCancel(po);
                } else if (Integer.valueOf(SettleStateEnum.DEAL.getCode()).equals(po.getState())) {//已处理
                    payDispatcher.invalid(po, param);
                }
            } else {
                //退款作废待实现
            }
        }
    }
}