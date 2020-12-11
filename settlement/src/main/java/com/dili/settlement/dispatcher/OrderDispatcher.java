package com.dili.settlement.dispatcher;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.settle.OrderService;
import com.dili.ss.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 结算单操作分发
 */
@Component
public class OrderDispatcher {

    @Autowired
    private List<OrderService> serviceList;

    private Map<Integer, OrderService> serviceMap = new ConcurrentHashMap<>();

    /**
     * 初始化方法
     */
    @PostConstruct
    public void init() {
        for (OrderService validateService : serviceList) {
            serviceMap.put(validateService.supportType(), validateService);
        }
    }

    /**
     * 保存
     * @param settleOrderDto
     * @return
     */
    public SettleOrder save(SettleOrderDto settleOrderDto) {
        return determineService(settleOrderDto.getType()).save(settleOrderDto);
    }

    /**
     * 保存
     * @param settleOrder
     * @return
     */
    public void cancel(SettleOrder settleOrder) {
        determineService(settleOrder.getType()).cancel(settleOrder);
    }

    /**
     * 根据结算方式决定service
     * @param type
     * @return
     */
    private OrderService determineService(Integer type) {
        OrderService service = serviceMap.get(type);
        if (service == null) {
            throw new BusinessException("", "不支持该结算类型");
        }
        return service;
    }
}
