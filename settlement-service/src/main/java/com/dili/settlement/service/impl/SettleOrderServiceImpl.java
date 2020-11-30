package com.dili.settlement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.dispatcher.PayDispatcher;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleOrderLink;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleFeeItemTypeEnum;
import com.dili.settlement.mapper.SettleOrderMapper;
import com.dili.settlement.service.*;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.BusinessException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
    private ApplicationConfigService applicationConfigService;
    @Autowired
    private SettleWayDetailService settleWayDetailService;
    @Autowired
    private PayDispatcher settleDispatchHandler;
    @Autowired
    private SettleFeeItemService settleFeeItemService;
    @Autowired
    private SettleOrderLinkService settleOrderLinkService;

    public SettleOrderMapper getActualDao() {
        return (SettleOrderMapper)getDao();
    }

    @Transactional
    @Override
    public SettleOrder save(SettleOrderDto settleOrderDto) {
        if (existsOrderCode(settleOrderDto.getAppId(), settleOrderDto.getOrderCode())) {
            throw new BusinessException("", "业务单号已存在");
        }
        SettleOrder settleOrder = BeanUtil.toBean(settleOrderDto, SettleOrder.class);
        insertSelective(settleOrder);
        //保存缴费项列表
        for (SettleFeeItem feeItem : settleOrderDto.getSettleFeeItemList()) {
            feeItem.setSettleOrderId(settleOrder.getId());
            feeItem.setType(SettleFeeItemTypeEnum.PAY_ITEM.getCode());
        }
        settleFeeItemService.batchInsert(settleOrderDto.getSettleFeeItemList());
        //保存链接列表
        for (SettleOrderLink orderLink : settleOrderDto.getSettleOrderLinkList()) {
            orderLink.setSettleOrderId(settleOrder.getId());
        }
        settleOrderLinkService.batchInsert(settleOrderDto.getSettleOrderLinkList());
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
    public Long queryTotalAmount(SettleOrderDto settleOrderDto) {
        return getActualDao().queryTotalAmount(settleOrderDto);
    }

    @Transactional
    @Override
    public int updateSettle(SettleOrder po) {
        return getActualDao().updateSettle(po);
    }
}