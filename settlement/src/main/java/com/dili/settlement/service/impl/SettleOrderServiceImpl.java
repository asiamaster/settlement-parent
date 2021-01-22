package com.dili.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.dispatcher.OrderDispatcher;
import com.dili.settlement.dispatcher.PayDispatcher;
import com.dili.settlement.dispatcher.RefundDispatcher;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.ChargeDateDto;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleAmountDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.ReverseEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.mapper.SettleOrderMapper;
import com.dili.settlement.service.SettleOrderService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.BusinessException;
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
    private RefundDispatcher refundDispatcher;

    @Autowired
    private OrderDispatcher orderDispatcher;

    public SettleOrderMapper getActualDao() {
        return (SettleOrderMapper)getDao();
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
        doCancel(get(id));
    }

    /**
     * 执行取消
     * @param po
     */
    private void doCancel(SettleOrder po) {
        if (po == null) {
            throw new BusinessException("", "未查询到结算单记录");
        }
        orderDispatcher.cancel(po);
    }

    @Transactional
    @Override
    public void cancelByCode(String code) {
        doCancel(getActualDao().getByCode(code));
    }

    @Transactional
    @Override
    public void cancel(Long appId, String orderCode) {
        doCancel(get(appId, orderCode));
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
            if (Integer.valueOf(SettleStateEnum.WAIT_DEAL.getCode()).equals(po.getState())) {//待处理
                doCancel(po);
            } else {
                if (Integer.valueOf(SettleTypeEnum.PAY.getCode()).equals(po.getType())) {//支付单
                    payDispatcher.invalid(po, param);
                } else {//退款单 逻辑未实现
                    refundDispatcher.invalid(po, param);
                }
            }
        }
    }

    @Override
    public int deleteById(Long id, Integer version) {
        return getActualDao().deleteById(id, version);
    }

    @Transactional
    @Override
    public int updateChargeDate(ChargeDateDto chargeDateDto) {
        return getActualDao().updateChargeDate(chargeDateDto);
    }
}