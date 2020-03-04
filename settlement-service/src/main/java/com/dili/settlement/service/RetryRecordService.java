package com.dili.settlement.service;

import com.dili.settlement.domain.RetryRecord;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-04 11:09:31.
 */
public interface RetryRecordService extends BaseService<RetryRecord, Long> {

    /**
     * 执行结算回调
     */
    void executeCallback();
}