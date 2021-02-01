package com.dili.settlement.mapper;

import com.dili.settlement.domain.SettleConfig;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettleConfigMapper extends MyMapper<SettleConfig> {

    /**
     * 查询结算配置列表
     * @param query
     * @return
     */
    List<SettleConfig> list(SettleConfig query);

    /**
     * 根据市场ID、组编码、编码获取唯一值
     * @param marketId
     * @param groupCode
     * @param code
     * @return
     */
    SettleConfig get(@Param("marketId") Long marketId, @Param("groupCode") Integer groupCode, @Param("code") Integer code);

    /**
     * 根据ID修改
     * @param config
     * @return
     */
    int updateById(SettleConfig config);
}