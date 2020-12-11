package com.dili.settlement.service.impl;

import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.enums.ConfigStateEnum;
import com.dili.settlement.enums.SettleGroupCodeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.SettleConfigService;
import com.dili.settlement.service.SettleWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 结算方式相关实现类
 */
@Service
public class SettleWayServiceImpl implements SettleWayService {

    @Autowired
    private SettleConfigService settleConfigService;

    @Override
    public List<SettleConfig> payChooseList(Long marketId, boolean multi) {
        List<SettleConfig> itemList = list(marketId, SettleGroupCodeEnum.SETTLE_WAY_PAY.getCode());
        if (multi) {
            itemList.removeIf(settleConfig -> settleConfig.getCode().equals(SettleWayEnum.MIXED_PAY.getCode()));
        }
        return itemList;
    }

    @Override
    public List<SettleConfig> payFormList(Long marketId) {
        List<SettleConfig> itemList = list(marketId, SettleGroupCodeEnum.SETTLE_WAY_PAY.getCode());
        itemList.removeIf(settleConfig -> settleConfig.getCode().equals(SettleWayEnum.MIXED_PAY.getCode()) || settleConfig.getCode().equals(SettleWayEnum.VIRTUAL_PAY.getCode()) || settleConfig.getCode().equals(SettleWayEnum.CARD.getCode()));
        return itemList;
    }

    @Override
    public List<SettleConfig> refundChooseList(Long firmId) {
        List<SettleConfig> itemList = list(firmId, SettleGroupCodeEnum.SETTLE_WAY_REFUND.getCode());
        return itemList;
    }

    /**
     * 公共查询方法
     * @return
     */
    private List<SettleConfig> list(Long marketId, int groupCode) {
        SettleConfig settleConfig = new SettleConfig();
        settleConfig.setMarketId(marketId);
        settleConfig.setGroupCode(groupCode);
        settleConfig.setState(ConfigStateEnum.ENABLE.getCode());
        List<SettleConfig> settleConfigList = settleConfigService.list(settleConfig);
        return settleConfigList != null ? settleConfigList : new ArrayList<>(0);
    }
}
