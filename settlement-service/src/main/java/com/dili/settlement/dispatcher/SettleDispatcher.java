package com.dili.settlement.dispatcher;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleOrderDto;
import org.springframework.ui.ModelMap;

/**
 * 结算分发器
 */
public interface SettleDispatcher {

    /**
     * 跳转到结算个性化页面
     * @param settleOrderDto
     * @param modelMap
     * @return
     */
    String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap);

    /**
     * 发起结算
     * @param po
     * @param settleOrderDto
     */
    void settle(SettleOrder po, SettleOrderDto settleOrderDto);

    /**
     * 作废
     * @param po
     * @param invalidRequestDto
     */
    void invalid(SettleOrder po, InvalidRequestDto invalidRequestDto);
}
