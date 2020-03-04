package com.dili.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.domain.ApplicationConfig;
import com.dili.settlement.domain.RetryRecord;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.*;
import com.dili.settlement.mapper.SettleOrderMapper;
import com.dili.settlement.service.*;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.BusinessException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-05 16:39:11.
 */
@Service
public class SettleOrderServiceImpl extends BaseServiceImpl<SettleOrder, Long> implements SettleOrderService {

    @Resource
    private FundAccountService fundAccountService;
    @Resource
    private ApplicationConfigService applicationConfigService;
    @Resource
    private MarketApplicationService marketApplicationService;
    @Resource
    private RetryRecordService retryRecordService;

    public SettleOrderMapper getActualDao() {
        return (SettleOrderMapper)getDao();
    }

    @Transactional
    @Override
    public void save(SettleOrder settleOrder) {
        if (!marketApplicationService.existsApp(settleOrder.getMarketId(), settleOrder.getAppId())) {
            throw new BusinessException("", "该应用未允许接入");
        }
        if (existsBusinessCode(settleOrder.getAppId(), settleOrder.getBusinessCode())) {
            throw new BusinessException("", "业务单号已存在");
        }
        insertSelective(settleOrder);
    }

    @Override
    public boolean existsBusinessCode(Long appId, String businessCode) {
        SettleOrderDto query = new SettleOrderDto();
        query.setAppId(appId);
        query.setBusinessCode(businessCode);
        List<SettleOrder> itemList = getActualDao().list(query);
        return !CollUtil.isEmpty(itemList);
    }

    @Transactional
    @Override
    public void cancelById(Long id) {
        SettleOrder po = get(id);
        if (po == null) {
            throw new BusinessException("", "未查询到结算单记录");
        }
        if (!po.getState().equals(SettleStateEnum.WAIT_DEAL.getCode())) {
            throw new BusinessException("", "非待处理的结算单无法取消");
        }
        po.setState(SettleStateEnum.CANCEL.getCode());
        int i = getActualDao().updateState(po);
        if (i != 1) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
    }

    @Transactional
    @Override
    public void cancelByCode(String code) {
        SettleOrder po = getActualDao().getByCode(code);
        if (po == null) {
            throw new BusinessException("", "未查询到结算单记录");
        }
        if (!po.getState().equals(SettleStateEnum.WAIT_DEAL.getCode())) {
            throw new BusinessException("", "非待处理的结算单无法取消");
        }
        po.setState(SettleStateEnum.CANCEL.getCode());
        int i = getActualDao().updateState(po);
        if (i != 1) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
    }

    @Transactional
    @Override
    public void cancel(Long appId, String businessCode) {
        SettleOrderDto query = new SettleOrderDto();
        query.setAppId(appId);
        query.setBusinessCode(businessCode);
        List<SettleOrder> itemList = getActualDao().list(query);
        if (CollUtil.isEmpty(itemList)) {
            throw new BusinessException("", "未查询到结算单记录");
        }
        SettleOrder po = itemList.get(0);
        if (!po.getState().equals(SettleStateEnum.WAIT_DEAL.getCode())) {
            throw new BusinessException("", "非待处理的结算单无法取消");
        }
        po.setState(SettleStateEnum.CANCEL.getCode());
        int i = getActualDao().updateState(po);
        if (i != 1) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
    }

    @Override
    public List<SettleOrder> list(SettleOrderDto query) {
        List<SettleOrder> itemList = getActualDao().list(query);
        if (Boolean.TRUE.equals(query.getConvert())) {
            convert(itemList);
        }
        return itemList;
    }

    /**
     * 进行数据转换
     * @param itemList
     */
    private void convert(List<SettleOrder> itemList) {
        if (CollUtil.isEmpty(itemList)) {
            return;
        }
        Map<Long, List<ApplicationConfig>> configs = new HashMap<>();
        for (SettleOrder po : itemList) {
            List<ApplicationConfig> configList = configs.get(po.getAppId());
            if (configList == null) {
                configList = applicationConfigService.list(po.getAppId(), AppGroupCodeEnum.APP_BUSINESS_TYPE.getCode());
                configs.put(po.getMarketId(), configList);
            }
            ApplicationConfig config = configList.stream().filter(temp -> po.getBusinessType().equals(temp.getCode())).findFirst().orElse(null);
            po.setBusinessName(config != null ? config.getVal() : "");
            po.setTypeName(SettleTypeEnum.getNameByCode(po.getType()));
            po.setStateName(SettleStateEnum.getNameByCode(po.getState()));
            po.setWayName(po.getWay() != null ? SettleWayEnum.getNameByCode(po.getWay()) : "");
        }
    }

    @Override
    public PageOutput<List<SettleOrder>> listPagination(SettleOrderDto query) {
        PageHelper.startPage(query.getPage(), query.getRows());
        List<SettleOrder> itemList = getActualDao().list(query);
        if (Boolean.TRUE.equals(query.getConvert())) {
            convert(itemList);
        }

        Page<SettleOrder> page = (Page)itemList;
        PageOutput<List<SettleOrder>> output = PageOutput.success();
        output.setData(itemList);
        output.setPageNum(page.getPageNum());
        output.setPageSize(page.getPageSize());
        output.setTotal(Long.valueOf(page.getTotal()).intValue());
        output.setStartRow(page.getStartRow());
        output.setEndRow(page.getEndRow());
        output.setPages(page.getPages());
        return output;
    }

    @Override
    public SettleOrder getByCode(String code) {
        return getActualDao().getByCode(code);
    }

    @Override
    public SettleOrder get(Long appId, String businessCode) {
        SettleOrderDto query = new SettleOrderDto();
        query.setAppId(appId);
        query.setBusinessCode(businessCode);
        return listByExample(query).stream().findFirst().orElse(null);
    }

    @Transactional
    @Override
    public void pay(SettleOrder po, SettleOrderDto settleOrderDto) {
        if (!po.getState().equals(SettleStateEnum.WAIT_DEAL.getCode())) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
        //way;operatorId;operatorName;operateTime;serialNumber,notes;
        po.setState(SettleStateEnum.DEAL.getCode());
        po.setWay(settleOrderDto.getWay());
        po.setOperatorId(settleOrderDto.getOperatorId());
        po.setOperatorName(settleOrderDto.getOperatorName());
        po.setOperateTime(DateUtil.nowDateTime());
        po.setSerialNumber(settleOrderDto.getSerialNumber());
        po.setNotes(settleOrderDto.getNotes());
        int i = getActualDao().updateSettle(po);
        if (i != 1) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
        fundAccountService.add(po.getMarketId(), po.getAppId(), po.getAmount());
        //存入回调重试记录  方便定时任务扫描
        RetryRecord retryRecord = new RetryRecord(RetryTypeEnum.SETTLE_CALLBACK.getCode(), po.getId(), po.getCode());
        retryRecordService.insertSelective(retryRecord);
        po.setRetryRecordId(retryRecord.getId());
    }

    @Transactional
    @Override
    public void refund(SettleOrder po, SettleOrderDto settleOrderDto) {
        if (!po.getState().equals(SettleStateEnum.WAIT_DEAL.getCode())) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
        //way,state,operatorId,operatorName,operateTime,accountNumber,bankName,bankCardHolder,serialNumber,notes
        po.setState(SettleStateEnum.DEAL.getCode());
        po.setWay(settleOrderDto.getWay());
        po.setOperatorId(settleOrderDto.getOperatorId());
        po.setOperatorName(settleOrderDto.getOperatorName());
        po.setOperateTime(DateUtil.nowDateTime());
        po.setAccountNumber(settleOrderDto.getAccountNumber());
        po.setBankName(settleOrderDto.getBankName());
        po.setBankCardHolder(settleOrderDto.getBankCardHolder());
        po.setSerialNumber(settleOrderDto.getSerialNumber());
        po.setNotes(settleOrderDto.getNotes());
        int i = getActualDao().updateSettle(po);
        if (i != 1) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
        fundAccountService.sub(po.getMarketId(), po.getAppId(), po.getAmount());
        //存入回调重试记录  方便定时任务扫描
        RetryRecord retryRecord = new RetryRecord(RetryTypeEnum.SETTLE_CALLBACK.getCode(), po.getId(), po.getCode());
        retryRecordService.insertSelective(retryRecord);
        po.setRetryRecordId(retryRecord.getId());
    }

    @Override
    public Long queryTotalAmount(SettleOrderDto settleOrderDto) {
        return getActualDao().queryTotalAmount(settleOrderDto);
    }
}