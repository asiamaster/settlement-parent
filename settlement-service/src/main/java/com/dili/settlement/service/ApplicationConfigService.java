package com.dili.settlement.service;

import com.dili.settlement.domain.ApplicationConfig;
import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.ss.base.BaseService;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-25 10:34:34.
 */
public interface ApplicationConfigService extends BaseService<ApplicationConfig, Long> {

    /**
     * 根据应用id 分组编码 编码获取唯一加密秘钥(注意数据库数据配置)
     * @param appId
     * @param groupCode
     * @param code
     * @return
     */
    String getVal(Long appId, int groupCode, int code);

    /**
     * 查询配置列表
     * @param appId
     * @param groupCode
     * @return
     */
    List<ApplicationConfig> list(Long appId, int groupCode);

    /**
     * 查询配置列表
     * @param applicationConfigDto
     * @return
     */
    List<ApplicationConfig> list(ApplicationConfigDto applicationConfigDto);
}