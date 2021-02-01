package com.dili.settlement.service.impl;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.enums.ConfigStateEnum;
import com.dili.settlement.mapper.SettleConfigMapper;
import com.dili.settlement.service.SettleConfigService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-07 11:11:14.
 */
@Service
public class SettleConfigServiceImpl extends BaseServiceImpl<SettleConfig, Long> implements SettleConfigService {

    public SettleConfigMapper getActualDao() {
        return (SettleConfigMapper)getDao();
    }

    @Override
    public List<SettleConfig> list(SettleConfig query) {
        return getActualDao().list(query);
    }

    @Override
    public SettleConfig get(Long marketId, Integer groupCode, Integer code) {
        return getActualDao().get(marketId, groupCode, code);
    }

    @Transactional
    @Override
    public int enable(SettleConfig config) {
        SettleConfig po = get(config.getMarketId(), config.getGroupCode(), config.getCode());
        if (po == null) {
            throw new BusinessException("", "该记录不存在");
        }
        po.setState(ConfigStateEnum.ENABLE.getCode());
        return getActualDao().updateById(po);
    }

    @Transactional
    @Override
    public int disable(SettleConfig config) {
        SettleConfig po = get(config.getMarketId(), config.getGroupCode(), config.getCode());
        if (po == null) {
            throw new BusinessException("", "该记录不存在");
        }
        po.setState(ConfigStateEnum.DISABLE.getCode());
        return getActualDao().updateById(po);
    }

    @Transactional
    @Override
    public void add(SettleConfig config) {
        insertSelective(config);
    }

    @Transactional
    @Override
    public int change(SettleConfig config) {
        SettleConfig po = get(config.getMarketId(), config.getGroupCode(), config.getCode());
        if (po == null) {
            throw new BusinessException("", "该记录不存在");
        }
        po.setVal(StrUtil.isBlank(config.getVal()) ? po.getVal() : config.getVal());
        po.setState(config.getState() == null ? po.getState() : config.getState());
        po.setNotes(StrUtil.isBlank(config.getNotes()) ? po.getNotes() : config.getNotes());
        po.setSortField(config.getSortField() == null ? po.getSortField() : config.getSortField());
        return getActualDao().updateById(po);
    }
}