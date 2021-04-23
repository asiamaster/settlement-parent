package com.dili.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.config.CallbackConfiguration;
import com.dili.settlement.domain.RetryRecord;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.mapper.RetryRecordMapper;
import com.dili.settlement.service.RetryRecordService;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.task.AsyncTaskExecutor;
import com.dili.settlement.task.CallbackRetryTask;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-04 11:09:31.
 */
@Service
public class RetryRecordServiceImpl extends BaseServiceImpl<RetryRecord, Long> implements RetryRecordService {

    public RetryRecordMapper getActualDao() {
        return (RetryRecordMapper)getDao();
    }

    @Autowired
    private SettleOrderService settleOrderService;

    @Autowired
    private CallbackConfiguration callbackConfiguration;

    @Override
    public void executeCallback() {
        RetryRecord query = new RetryRecord();
        List<RetryRecord> itemList = listByExample(query);
        if (CollUtil.isEmpty(itemList)) {
            return;
        }
        for (RetryRecord retryRecord : itemList) {
            SettleOrder settleOrder = settleOrderService.get(retryRecord.getId());
            if (settleOrder == null) {//当结算单记录不存在，删除对应的重试任务记录
                delete(retryRecord.getId());
                continue;
            }
            AsyncTaskExecutor.submit(new CallbackRetryTask(callbackConfiguration.getTimes(), callbackConfiguration.getIntervalMills(), settleOrderService, settleOrder));
        }
    }

    @Transactional
    @Override
    public int batchInsert(List<RetryRecord> retryRecordList) {
        if (CollUtil.isEmpty(retryRecordList)) {
            return 0;
        }
        return getActualDao().batchInsert(retryRecordList);
    }
}