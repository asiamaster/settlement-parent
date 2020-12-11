package com.dili.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.settlement.dto.CustomerAccountSerialDto;
import com.dili.settlement.mapper.CustomerAccountSerialMapper;
import com.dili.settlement.service.CustomerAccountSerialService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

    @Override
    public PageOutput<List<CustomerAccountSerialDto>> listPagination(CustomerAccountSerialDto query) {
        PageHelper.startPage(query.getPage(), query.getRows());
        List<CustomerAccountSerialDto> itemList = getActualDao().list(query);

        Page<CustomerAccountSerialDto> page = (Page)itemList;
        PageOutput<List<CustomerAccountSerialDto>> output = PageOutput.success();
        output.setData(itemList);
        output.setPageNum(page.getPageNum());
        output.setPageSize(page.getPageSize());
        output.setTotal(page.getTotal());
        output.setStartRow(page.getStartRow());
        output.setEndRow(page.getEndRow());
        output.setPages(page.getPages());
        return output;
    }
}