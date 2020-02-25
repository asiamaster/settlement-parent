package com.dili.settlement.mapper;

import com.dili.settlement.domain.ApplicationConfig;
import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.ss.base.MyMapper;

import java.util.List;

public interface ApplicationConfigMapper extends MyMapper<ApplicationConfig> {
    /**
     * 查询应用配置列表
     * @param applicationConfigDto
     * @return
     */
    List<ApplicationConfig> list(ApplicationConfigDto applicationConfigDto);
}