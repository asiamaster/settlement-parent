package com.dili.settlement.service;

import com.dili.settlement.domain.SettleConfig;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-07 11:11:14.
 */
public interface SettleConfigService extends BaseService<SettleConfig, Long> {

    /**
     * 根据市场ID、组编码、编码获取唯一值
     * @param marketId
     * @param groupCode
     * @param code
     * @return
     */
    SettleConfig get(Long marketId, Integer groupCode, Integer code);

    /**
     * 根据市场ID、组编码、编码启用
     * @param config
     * @return
     */
    int enable(SettleConfig config);

    /**
     * 根据市场ID、组编码、编码禁用
     * @param config
     * @return
     */
    int disable(SettleConfig config);

    /**
     * 根据市场ID、组编码、编码 新增
     * @param config
     */
    void add(SettleConfig config);

    /**
     * 根据市场ID、组编码、编码 修改
     * @param config
     * @return
     */
    int change(SettleConfig config);
}