package com.dili.settlement.rpc;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 结算相关rpc
 */
@FeignClient(name = "settlement", contextId = "settleOrderRpc", url="${SettleRpc.url:}")
public interface SettleOrderRpc {

    /**
     * 查询结算单列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/list")
    BaseOutput<List<SettleOrder>> list(@RequestBody SettleOrderDto query);

    /**
     * 提交结算单接口
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/submit")
    BaseOutput<SettleOrder> submit(SettleOrderDto settleOrderDto);

    /**
     * 根据结算单id取消
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/cancelById")
    BaseOutput<String> cancelById(@RequestParam Long id);

    /**
     * 根据结算单编号取消
     * @param code
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/cancelByCode")
    BaseOutput<String> cancelByCode(@RequestParam String code);

    /**
     * 根据appId businessCode取消
     * @param appId 应用ID
     * @param orderCode 订单号
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/cancel")
    BaseOutput<String> cancel(@RequestParam Long appId, @RequestParam String orderCode);

    /**
     * 根据id查询结算单
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/getById")
    BaseOutput<SettleOrder> getById(@RequestParam Long id);

    /**
     * 根据结算单号查询结算单
     * @param code
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/getByCode")
    BaseOutput<SettleOrder> getByCode(@RequestParam String code);

    /**
     * 根据appId businessCode 查询结算单
     * @param appId 应用ID
     * @param orderCode 业务编号
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/get")
    BaseOutput<SettleOrder> get(@RequestParam Long appId, @RequestParam String orderCode);

    /**
     * 用于作废业务接口
     * @param param
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/invalid")
    BaseOutput<?> invalid(InvalidRequestDto param);

    /**
     * @param settleOrderCode 根据结算单号查询结算明细列表
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/listByCode")
    BaseOutput<List<SettleWayDetail>> listByCode(@RequestParam String settleOrderCode);

    /**
     * 【查询】结算单结算详情 ---根据结算编号code查询
     * @param code 结算单code
     * @return
     */
    @RequestMapping(value = "/api/settleWayDetail/listByCode")
    BaseOutput<List<SettleWayDetail>> listSettleWayDetailsByCode(@RequestParam String code);
}
