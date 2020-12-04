package com.dili.settlement.dispatcher;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.settle.PayService;
import com.dili.ss.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 结算验证分发器
 */
@Component
public class PayDispatcher implements SettleDispatcher {

    @Autowired
    private List<PayService> serviceList;

    private Map<Integer, PayService> serviceMap = new ConcurrentHashMap<>();

    /**
     * 初始化方法
     */
    @PostConstruct
    public void init() {
        for (PayService validateService : serviceList) {
            serviceMap.put(validateService.supportWay(), validateService);
        }
    }

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return determineService(settleOrderDto.getWay()).forwardSpecial(settleOrderDto, modelMap);
    }

    @Override
    public List<SettleOrder> settle(SettleOrderDto settleOrderDto) {
        return determineService(settleOrderDto.getWay()).settle(settleOrderDto);
    }

    @Override
    public void invalid(SettleOrder po, InvalidRequestDto invalidRequestDto) {
        //determineService(po.getWay()).invalid(po, invalidRequestDto);
    }

    /**
     * 根据结算方式决定service
     * @param way
     * @return
     */
    private PayService determineService(Integer way) {
        PayService service = serviceMap.get(way);
        if (service == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        return service;
    }
}
