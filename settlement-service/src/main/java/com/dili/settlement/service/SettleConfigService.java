package com.dili.settlement.service;

import com.dili.settlement.domain.SettleConfig;
import com.dili.ss.base.BaseService;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-07 11:11:14.
 */
public interface SettleConfigService extends BaseService<SettleConfig, Long> {
    /**
     * 根据分组编码 编码获取唯一加密秘钥(注意数据库数据配置)
     * @param groupCode
     * @param code
     * @return
     */
    String getVal(int groupCode, int code);

    /**
     * 查询配置列表
     * @param groupCode
     * @return
     */
    List<SettleConfig> list(int groupCode);
}