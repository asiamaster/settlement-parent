package com.dili.settlement.service.impl;

import com.dili.settlement.domain.ApplicationConfig;
import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.settlement.mapper.ApplicationConfigMapper;
import com.dili.settlement.service.ApplicationConfigService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-25 10:34:34.
 */
@Service
public class ApplicationConfigServiceImpl extends BaseServiceImpl<ApplicationConfig, Long> implements ApplicationConfigService {

    public ApplicationConfigMapper getActualDao() {
        return (ApplicationConfigMapper)getDao();
    }

    @Override
    public String getVal(Long appId, int groupCode, int code) {
        ApplicationConfig query = new ApplicationConfig();
        query.setGroupCode(groupCode);
        query.setCode(code);
        //获取单个值考虑到历史数据问题   所以不带状态条件
        //query.setState(ConfigStateEnum.ENABLE.getCode());
        ApplicationConfig po = listByExample(query).stream().findFirst().orElse(null);
        return po != null ? po.getVal() : null;
    }

    @Override
    public List<ApplicationConfig> list(Long appId, int groupCode) {
        ApplicationConfig query = new ApplicationConfig();
        query.setGroupCode(groupCode);
        return listByExample(query);
    }

    @Override
    public List<ApplicationConfig> list(ApplicationConfigDto applicationConfigDto) {
        return getActualDao().list(applicationConfigDto);
    }
}