package com.dili.settlement.service.impl;

import com.dili.settlement.domain.RetryError;
import com.dili.settlement.mapper.RetryErrorMapper;
import com.dili.settlement.service.RetryErrorService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-04 11:16:38.
 */
@Service
public class RetryErrorServiceImpl extends BaseServiceImpl<RetryError, Long> implements RetryErrorService {

    public RetryErrorMapper getActualDao() {
        return (RetryErrorMapper)getDao();
    }
}