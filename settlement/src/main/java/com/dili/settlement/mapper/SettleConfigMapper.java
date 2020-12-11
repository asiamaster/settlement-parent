package com.dili.settlement.mapper;

import com.dili.settlement.domain.SettleConfig;
import com.dili.ss.base.MyMapper;

import java.util.List;

public interface SettleConfigMapper extends MyMapper<SettleConfig> {

    /**
     * 查询结算配置列表
     * @param query
     * @return
     */
    List<SettleConfig> list(SettleConfig query);
}