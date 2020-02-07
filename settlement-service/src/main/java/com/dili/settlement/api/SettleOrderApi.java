package com.dili.settlement.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
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
import java.util.List;

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

    /**
     * 提交结算单接口
     * @param settleOrder
     * @return
     */
    @RequestMapping(value = "/save")
    public BaseOutput<SettleOrder> save(@RequestBody SettleOrder settleOrder) {
        try {
            validSaveParameters(settleOrder);
            settleOrder.setCode(codeService.generate());
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
            return BaseOutput.success().setData(po);
        } catch (Exception e) {
            LOGGER.error("method getByCode", e);
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
            if (CollUtil.isEmpty(settleOrderDto.getIdList())) {
                return BaseOutput.failure("ID列表为空");
            }
            SettleResultDto settleResultDto = new SettleResultDto();
            settleResultDto.setTotalNum(settleOrderDto.getIdList().size());
            for (Long id : settleOrderDto.getIdList()) {
                SettleOrder po = settleOrderService.get(id);
                if (po == null) {
                    settleResultDto.failure(null);
                    continue;
                }
                try {
                    settleOrderService.pay(po, settleOrderDto);
                    settleResultDto.success(po);
                } catch (Exception e) {
                    LOGGER.error(po.getCode(), e);
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
            if (CollUtil.isEmpty(settleOrderDto.getIdList())) {
                return BaseOutput.failure("ID列表为空");
            }
            SettleResultDto settleResultDto = new SettleResultDto();
            settleResultDto.setTotalNum(settleOrderDto.getIdList().size());
            for (Long id : settleOrderDto.getIdList()) {
                SettleOrder po = settleOrderService.get(id);
                if (po == null) {
                    settleResultDto.failure(null);
                    continue;
                }
                try {
                    settleOrderService.refund(po, settleOrderDto);
                    settleResultDto.success(po);
                } catch (Exception e) {
                    LOGGER.error(po.getCode(), e);
                    settleResultDto.failure(po);
                }
            }
            return BaseOutput.success().setData(settleResultDto);
        } catch (Exception e) {
            LOGGER.error("method refund", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 验证保存接口参数
     * @param settleOrder
     */
    private void validSaveParameters(SettleOrder settleOrder) {
        if (settleOrder.getMarketId() == null) {
            throw new BusinessException("", "市场ID为空");
        }
        if (settleOrder.getAppId() == null) {
            throw new BusinessException("", "应用ID为空");
        }
        if (settleOrder.getBusinessType() == null) {
            throw new BusinessException("", "业务类型为空");
        }
        if (StrUtil.isBlank(settleOrder.getBusinessCode())) {
            throw new BusinessException("", "业务单号为空");
        }
        if (settleOrder.getCustomerId() == null) {
            throw new BusinessException("", "客户ID为空");
        }
        if (StrUtil.isBlank(settleOrder.getCustomerName())) {
            throw new BusinessException("", "客户姓名为空");
        }
        if (StrUtil.isBlank(settleOrder.getCustomerPhone())) {
            throw new BusinessException("", "客户手机号为空");
        }
        if (settleOrder.getType() == null) {
            throw new BusinessException("", "结算类型为空");
        }
        if (settleOrder.getAmount() == null || settleOrder.getAmount() < 0) {
            throw new BusinessException("", "金额不合法");
        }
        if (settleOrder.getBusinessDepId() == null) {
            throw new BusinessException("", "业务部门ID为空");
        }
        if (StrUtil.isBlank(settleOrder.getBusinessDepName())) {
            throw new BusinessException("", "业务部门名称为空");
        }
        if (settleOrder.getSubmitterId() == null) {
            throw new BusinessException("", "提交人ID为空");
        }
        if (StrUtil.isBlank(settleOrder.getSubmitterName())) {
            throw new BusinessException("", "提交人姓名为空");
        }
        if (settleOrder.getSubmitterDepId() == null) {
            throw new BusinessException("", "提交人部门ID为空");
        }
        if (StrUtil.isBlank(settleOrder.getBusinessDepName())) {
            throw new BusinessException("", "提交人部门名称为空");
        }
        if (StrUtil.isBlank(settleOrder.getReturnUrl())) {
            throw new BusinessException("", "回调路径为空");
        }
    }
}
