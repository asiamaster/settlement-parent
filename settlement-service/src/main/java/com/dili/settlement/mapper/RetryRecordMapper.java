package com.dili.settlement.mapper;

import com.dili.settlement.domain.RetryRecord;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RetryRecordMapper extends MyMapper<RetryRecord> {
    /**
     * 批量insert
     * @param retryRecordList
     * @return
     */
    int batchInsert(@Param("retryRecordList") List<RetryRecord> retryRecordList);
}