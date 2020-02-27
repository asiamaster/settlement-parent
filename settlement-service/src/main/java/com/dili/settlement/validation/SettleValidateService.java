package com.dili.settlement.validation;

import com.dili.settlement.dto.SettleOrderDto;

/**
 * 结算参数验证
 */
public interface SettleValidateService {

    /**
     * 验证提交参数
     * @param settleOrderDto
     */
    void validSubmitParams(SettleOrderDto settleOrderDto);

    /**
     * 验证结算参数
     * @param settleOrderDto
     */
    void validSettleParams(SettleOrderDto settleOrderDto);

    /**
     * 个性化验证
     * @param settleOrderDto
     */
    void validParamsSpecial(SettleOrderDto settleOrderDto);

    /**
     * 支持的结算方式
     * @return
     */
    Integer supportWay();
}
