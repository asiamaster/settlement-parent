package com.dili.settlement.mapper;

import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerAccountSerialMapper extends MyMapper<CustomerAccountSerial> {

    /**
     * 批量保存流水
     * @param accountSerialList
     * @param customerAccountId
     * @return
     */
    int batchInsert(@Param("accountSerialList") List<CustomerAccountSerial> accountSerialList, @Param("customerAccountId") Long customerAccountId);
}