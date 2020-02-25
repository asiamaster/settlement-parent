package com.dili.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.domain.MarketApplication;
import com.dili.settlement.enums.ConfigStateEnum;
import com.dili.settlement.mapper.MarketApplicationMapper;
import com.dili.settlement.service.MarketApplicationService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-25 10:31:29.
 */
@Service
public class MarketApplicationServiceImpl extends BaseServiceImpl<MarketApplication, Long> implements MarketApplicationService {

    public MarketApplicationMapper getActualDao() {
        return (MarketApplicationMapper)getDao();
    }

    @Override
    public boolean existsApp(Long marketId, Long appId) {
        MarketApplication query = new MarketApplication();
        query.setMarketId(marketId);
        query.setAppId(appId);
        query.setState(ConfigStateEnum.ENABLE.getCode());
        List<MarketApplication> itemList = listByExample(query);
        return !CollUtil.isEmpty(itemList);
    }
}