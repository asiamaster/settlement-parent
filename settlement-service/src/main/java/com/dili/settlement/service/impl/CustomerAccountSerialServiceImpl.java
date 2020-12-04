package com.dili.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.settlement.mapper.CustomerAccountSerialMapper;
import com.dili.settlement.service.CustomerAccountSerialService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-12-03 18:15:40.
 */
@Service
public class CustomerAccountSerialServiceImpl extends BaseServiceImpl<CustomerAccountSerial, Long> implements CustomerAccountSerialService {

    public CustomerAccountSerialMapper getActualDao() {
        return (CustomerAccountSerialMapper)getDao();
    }

    @Transactional
    @Override
    public int batchInsert(List<CustomerAccountSerial> accountSerialList, Long customerAccountId) {
        if (CollUtil.isEmpty(accountSerialList)) {
            return 0;
        }
        return getActualDao().batchInsert(accountSerialList, customerAccountId);
    }
}