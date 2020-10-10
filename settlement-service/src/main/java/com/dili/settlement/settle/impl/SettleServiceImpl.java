package com.dili.settlement.settle.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.RetryRecord;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.RetryTypeEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.service.FundAccountService;
import com.dili.settlement.service.RetryRecordService;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.settle.SettleService;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 结算验证基础实现类
 */
public abstract class SettleServiceImpl implements SettleService {

    @Autowired
    protected SettleOrderService settleOrderService;

    @Autowired
    protected RetryRecordService retryRecordService;

    @Autowired
    protected FundAccountService fundAccountService;

    @Override
    public void validSubmitParams(SettleOrderDto settleOrderDto) {
        validParamsSpecial(settleOrderDto);
    }

    @Override
    public void validSettleParams(SettleOrderDto settleOrderDto) {
        if (CollUtil.isEmpty(settleOrderDto.getIdList())) {
            throw new BusinessException("", "ID列表为空");
        }
        if (settleOrderDto.getOperatorId() == null) {
            throw new BusinessException("", "结算员ID为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getOperatorName())) {
            throw new BusinessException("", "结算员为空");
        }
        validParamsSpecial(settleOrderDto);
    }

    @Override
    public void settleParam(SettleOrder po, SettleOrderDto settleOrderDto) {
        po.setState(SettleStateEnum.DEAL.getCode());
        po.setWay(settleOrderDto.getWay());
        po.setOperatorId(settleOrderDto.getOperatorId());
        po.setOperatorName(settleOrderDto.getOperatorName());
        po.setOperateTime(DateUtil.nowDateTime());
        po.setNotes(settleOrderDto.getNotes());
        settleParamSpecial(po, settleOrderDto);
    }

    @Override
    public void settleBefore(SettleOrder po, SettleOrderDto settleOrderDto) {
        if (!po.getState().equals(SettleStateEnum.WAIT_DEAL.getCode())) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
        settleParam(po, settleOrderDto);
    }

    @Transactional
    @Override
    public void settle(SettleOrder po, SettleOrderDto settleOrderDto) {
        //前置处理
        settleBefore(po, settleOrderDto);
        int i = settleOrderService.updateSettle(po);
        if (i != 1) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
        //存入回调重试记录  方便定时任务扫描
        RetryRecord retryRecord = new RetryRecord(RetryTypeEnum.SETTLE_CALLBACK.getCode(), po.getId(), po.getCode());
        retryRecordService.insertSelective(retryRecord);
        po.setRetryRecordId(retryRecord.getId());
        //后置处理
        settleAfter(po, settleOrderDto);
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        return;
    }

}
