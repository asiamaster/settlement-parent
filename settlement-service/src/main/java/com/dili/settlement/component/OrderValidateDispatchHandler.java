package com.dili.settlement.component;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.settle.OrderValidateService;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据验证分发器
 */
@Component
public class OrderValidateDispatchHandler {

    @Resource
    private List<OrderValidateService> validateServiceList;

    private Map<Integer, OrderValidateService> validateServiceMap = new ConcurrentHashMap<>();

    /**
     * 初始化方法
     */
    @PostConstruct
    public void init() {
        for (OrderValidateService service : validateServiceList) {
            validateServiceMap.put(service.supportType(), service);
        }
    }
    /**
     * 验证提交参数
     * @param settleOrderDto
     */
    public void validParams(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getType() == null) {
            throw new BusinessException("", "结算类型为空");
        }
        OrderValidateService validateService = validateServiceMap.get(settleOrderDto.getType());
        if (validateService == null) {
            throw new BusinessException("", "不支持该结算类型");
        }
        validateService.validParams(settleOrderDto);
    }
}
