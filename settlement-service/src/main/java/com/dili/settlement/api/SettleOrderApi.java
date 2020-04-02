package com.dili.settlement.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.settlement.component.CallbackHolder;
import com.dili.settlement.component.OrderValidateDispatchHandler;
import com.dili.settlement.component.SettleValidateDispatchHandler;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.settlement.enums.EditEnableEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.service.CodeService;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结算单相关api
 */
@RestController
@RequestMapping(value = "/api/settleOrder")
public class SettleOrderApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleOrderApi.class);

    @Resource
    private CodeService codeService;

    @Resource
    private SettleOrderService settleOrderService;

    @Resource
    private OrderValidateDispatchHandler orderValidateDispatchHandler;

    @Resource
    private SettleValidateDispatchHandler settleValidateDispatchHandler;
    /**
     * 提交结算单接口
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/save")
    public BaseOutput<SettleOrder> save(@RequestBody SettleOrderDto settleOrderDto) {
        try {
            orderValidateDispatchHandler.validParams(settleOrderDto);
            SettleOrder settleOrder = BeanUtil.toBean(settleOrderDto, SettleOrder.class);
            settleOrder.setCode(codeService.generate(settleOrderDto.getMarketCode().toUpperCase(), "settleOrder"));
            settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
            settleOrder.setSubmitTime(DateUtil.nowDateTime());
            settleOrder.setEditEnable(settleOrder.getEditEnable() == null ? EditEnableEnum.YES.getCode() : settleOrder.getEditEnable());
            settleOrderService.save(settleOrder);
            return BaseOutput.success().setData(settleOrder);
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOGGER.error("method save", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 根据结算单id取消
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancelById")
    public BaseOutput<String> cancelById(Long id) {
        try {
            if (id == null) {
                return BaseOutput.failure("结算单ID为空");
            }
            settleOrderService.cancelById(id);
            return BaseOutput.success();
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOGGER.error("method cancelById", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 根据结算单编号取消
     * @param code
     * @return
     */
    @RequestMapping(value = "/cancelByCode")
    public BaseOutput<String> cancelByCode(String code) {
        try {
            if (StrUtil.isBlank(code)) {
                return BaseOutput.failure("结算单号为空");
            }
            settleOrderService.cancelByCode(code);
            return BaseOutput.success();
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOGGER.error("method cancelByCode", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 根据appId businessCode取消
     * @param appId 应用ID
     * @param orderCode 订单号
     * @return
     */
    @RequestMapping(value = "/cancel")
    public BaseOutput<String> cancel(Long appId, String orderCode) {
        try {
            if (appId == null) {
                return BaseOutput.failure("应用ID为空");
            }
            if (StrUtil.isBlank(orderCode)) {
                return BaseOutput.failure("订单号为空");
            }
            settleOrderService.cancel(appId, orderCode);
            return BaseOutput.success();
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOGGER.error("method cancel", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 查询结算单列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list")
    public BaseOutput<List<SettleOrder>> list(@RequestBody SettleOrderDto query) {
        try {
            List<SettleOrder> itemList = settleOrderService.list(query);
            return BaseOutput.success().setData(itemList);
        } catch (Exception e) {
            LOGGER.error("method list", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 分页查询结算单列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/listPage")
    public PageOutput<List<SettleOrder>> listPage(@RequestBody SettleOrderDto query) {
        try {
            return settleOrderService.listPagination(query);
        } catch (Exception e) {
            LOGGER.error("method listPage", e);
            return PageOutput.failure();
        }
    }

    /**
     * 根据id查询结算单
     * @param id
     * @return
     */
    @RequestMapping(value = "/getById")
    public BaseOutput<SettleOrder> getById(Long id) {
        try {
            if (id == null) {
                return BaseOutput.failure("ID为空");
            }
            SettleOrder po = settleOrderService.get(id);
            if (po == null) {
                return BaseOutput.failure("未查询到结算单记录");
            }
            return BaseOutput.success().setData(po);
        } catch (Exception e) {
            LOGGER.error("method getById", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 根据结算单号查询结算单
     * @param code
     * @return
     */
    @RequestMapping(value = "/getByCode")
    public BaseOutput<SettleOrder> getByCode(String code) {
        try {
            if (StrUtil.isBlank(code)) {
                return BaseOutput.failure("结算单号为空");
            }
            SettleOrder po = settleOrderService.getByCode(code);
            if (po == null) {
                return BaseOutput.failure("未查询到结算单记录");
            }
            return BaseOutput.success().setData(po);
        } catch (Exception e) {
            LOGGER.error("method getByCode", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 根据appId businessCode 查询结算单
     * @param appId 应用ID
     * @param orderCode 业务编号
     * @return
     */
    @RequestMapping(value = "/get")
    public BaseOutput<SettleOrder> get(Long appId, String orderCode) {
        try {
            if (appId == null) {
                return BaseOutput.failure("应用ID为空");
            }
            if (StrUtil.isBlank(orderCode)) {
                return BaseOutput.failure("订单号为空");
            }
            SettleOrder po = settleOrderService.get(appId, orderCode);
            if (po == null) {
                return BaseOutput.failure("未查询到结算单记录");
            }
            return BaseOutput.success().setData(po);
        } catch (Exception e) {
            LOGGER.error("method get", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 根据appId businessCode 验证是否已结算
     * @param appId 应用ID
     * @param orderCode 订单号
     * @return
     */
    @RequestMapping(value = "/settleConfirm")
    public BaseOutput<Map<String, Object>> settleConfirm(Long appId, String orderCode) {
        try {
            if (appId == null) {
                return BaseOutput.failure("应用ID为空");
            }
            if (StrUtil.isBlank(orderCode)) {
                return BaseOutput.failure("订单号为空");
            }
            SettleOrder po = settleOrderService.get(appId, orderCode);
            if (po == null) {
                return BaseOutput.failure("未查询到结算单记录");
            }
            Map<String, Object> map = new HashMap<>(2);
            //返回true表示已结算 false 表示未结算
            map.put("flag", po.getState().equals(SettleStateEnum.DEAL.getCode()));
            map.put("settleOrder", po);
            return BaseOutput.success().setData(map);
        } catch (Exception e) {
            LOGGER.error("method settleConfirm", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 根据id列表查询总额
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/queryTotalAmount")
    public BaseOutput<Long> queryTotalAmount(@RequestBody SettleOrderDto settleOrderDto) {
        try {
            if (CollUtil.isEmpty(settleOrderDto.getIdList())) {
                return BaseOutput.failure("ID列表为空");
            }
            Long totalAmount = settleOrderService.queryTotalAmount(settleOrderDto);
            return BaseOutput.success().setData(totalAmount);
        } catch (Exception e) {
            LOGGER.error("method queryTotalAmount");
            return BaseOutput.failure();
        }
    }

    /**
     * 处理支付逻辑
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/pay")
    public BaseOutput<SettleResultDto> pay(@RequestBody SettleOrderDto settleOrderDto) {
        try {
            settleValidateDispatchHandler.validPayParams(settleOrderDto);
            SettleResultDto settleResultDto = new SettleResultDto();
            settleResultDto.setTotalNum(settleOrderDto.getIdList().size());
            for (Long id : settleOrderDto.getIdList()) {
                SettleOrder po = null;
                try {
                    po = settleOrderService.get(id);
                    if (po == null) {
                        settleResultDto.failure(null);
                        continue;
                    }
                    settleOrderService.pay(po, settleOrderDto);
                    CallbackHolder.offerSource(po);//触发回调
                    settleResultDto.success(po);
                } catch (Exception e) {
                    LOGGER.error(po != null ? po.getCode() : "", e);
                    settleResultDto.failure(po);
                }
            }
            return BaseOutput.success().setData(settleResultDto);
        } catch (Exception e) {
            LOGGER.error("method pay", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 处理退款逻辑
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/refund")
    public BaseOutput<SettleResultDto> refund(@RequestBody SettleOrderDto settleOrderDto) {
        try {
            settleValidateDispatchHandler.validRefundParams(settleOrderDto);
            SettleResultDto settleResultDto = new SettleResultDto();
            settleResultDto.setTotalNum(settleOrderDto.getIdList().size());
            for (Long id : settleOrderDto.getIdList()) {
                SettleOrder po = null;
                try {
                    po = settleOrderService.get(id);
                    if (po == null) {
                        settleResultDto.failure(null);
                        continue;
                    }
                    settleOrderService.refund(po, settleOrderDto);
                    CallbackHolder.offerSource(po);//触发回调
                    settleResultDto.success(po);
                } catch (Exception e) {
                    LOGGER.error(po != null ? po.getCode() : "", e);
                    settleResultDto.failure(po);
                }
            }
            return BaseOutput.success().setData(settleResultDto);
        } catch (Exception e) {
            LOGGER.error("method refund", e);
            return BaseOutput.failure();
        }
    }
}
