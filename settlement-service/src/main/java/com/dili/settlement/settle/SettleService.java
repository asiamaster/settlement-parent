package com.dili.settlement.settle;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleDataDto;
import com.dili.settlement.dto.SettleOrderDto;
import org.springframework.ui.ModelMap;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 结算参数验证
 */
public interface SettleService {

    //调用支付传递业务编号前缀
    String PAY_BUSINESS_PREFIX = "ZL_";

    /**
     * 跳转到结算方式个性化页面
     * @param settleOrderDto
     * @param modelMap
     * @return
     */
    String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap);

    /**
     * 结算
     * @param settleOrderDto
     * 注意：添加事务管理
     */
    List<SettleOrder> settle(SettleOrderDto settleOrderDto);

    /**
     * 结算 个性化处理
     * @param settleOrderList
     * @param settleOrderDto
     */
    void settleSpecial(List<SettleOrder> settleOrderList, SettleOrderDto settleOrderDto);

    /**
     * 验证是否可结算 锁定并返回待结算单列表
     * @param ids
     */
    List<SettleOrder> canSettle(List<Long> ids);

    /**
     * 验证结算提交参数
     * @param settleOrderDto
     */
    void validParams(SettleOrderDto settleOrderDto);

    /**
     * 验证结算提交参数 个性化
     * @param settleOrderDto
     */
    void validParamsSpecial(SettleOrderDto settleOrderDto);

    /**
     * 准备结算数据
     * @param settleOrderList
     * @param settleOrderDto
     * @return
     */
    SettleDataDto prepareSettleData(List<SettleOrder> settleOrderList, SettleOrderDto settleOrderDto);

    /**
     * 准备结算数据
     * @param settleOrderList
     * @param settleOrderDto
     * @param totalDeductAmount
     * @return
     */
    SettleDataDto prepareSettleData(List<SettleOrder> settleOrderList, SettleOrderDto settleOrderDto, Long totalDeductAmount);

    /**
     * 构建结算信息
     * @param settleOrder
     * @param settleOrderDto
     * @param localDateTime
     */
    void buildSettleInfo(SettleOrder settleOrder, SettleOrderDto settleOrderDto, LocalDateTime localDateTime);

    /**
     * 构建结算信息 个性化
     * @param settleOrder
     * @param settleOrderDto
     * @param localDateTime
     */
    void buildSettleInfoSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto, LocalDateTime localDateTime);

    /**
     * 返回交易资金账号ID
     * @param settleOrderDto
     * @return
     */
    Long getTradeFundAccountId(SettleOrderDto settleOrderDto);

    /**
     * 获取支付渠道
     * @return
     */
    Integer getTradeChannel();

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
     * @param param
     */
    void invalidSpecial(SettleOrder po, SettleOrder reverseOrder, InvalidRequestDto param);

    /**
     * 支持的结算方式
     * @return
     */
    Integer supportWay();
}
