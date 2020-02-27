package com.dili.settlement.validation;

import com.dili.settlement.dto.SettleOrderDto;

/**
 * 结算单提交验证接口
 */
public interface OrderValidateService {

    /**
     * 验证参数
     * @param settleOrderDto
     */
    void validParams(SettleOrderDto settleOrderDto);

    /**
     * 个性化验证参数
     * @param settleOrderDto
     */
    void validParamsSpecial(SettleOrderDto settleOrderDto);

    /**
     * 返回支持的结算类型
     * @return
     */
    Integer supportType();
}
