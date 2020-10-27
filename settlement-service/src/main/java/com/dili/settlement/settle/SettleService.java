package com.dili.settlement.settle;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleOrderDto;

/**
 * 结算参数验证
 */
public interface SettleService {

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
     * 设置结算参数
     * @param po
     * @param settleOrderDto
     */
    void settleParam(SettleOrder po, SettleOrderDto settleOrderDto);

    /**
     * 设置特殊结算参数
     * @param po
     * @param settleOrderDto
     */
    void settleParamSpecial(SettleOrder po, SettleOrderDto settleOrderDto);

    /**
     * 结算前置
     * @param po
     * @param settleOrderDto
     */
    void settleBefore(SettleOrder po, SettleOrderDto settleOrderDto);

    /**
     * 结算
     * @param po
     * @param settleOrderDto
     * 注意：添加事务管理
     */
    void settle(SettleOrder po, SettleOrderDto settleOrderDto);

    /**
     * 结算
     * @param po
     * @param settleOrderDto
     */
    void settleAfter(SettleOrder po, SettleOrderDto settleOrderDto);

    /**
     * 支持的结算方式
     * @return
     */
    Integer supportWay();

    /**
     * 作废
     * @param po
     * @param param
     */
    void invalid(SettleOrder po, InvalidRequestDto param);

    /**
     * 作废
     * @param po
     * @param reverseOrder
     */
    void invalidSpecial(SettleOrder po, SettleOrder reverseOrder);
}
