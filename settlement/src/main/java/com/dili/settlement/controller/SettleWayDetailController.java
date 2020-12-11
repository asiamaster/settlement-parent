package com.dili.settlement.controller;

import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.enums.ReverseEnum;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.service.SettleWayDetailService;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 结算方式明细
 */
@Controller
@RequestMapping(value = "/settleWayDetail")
public class SettleWayDetailController extends AbstractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleWayDetailController.class);

    @Autowired
    private SettleWayDetailService settleWayDetailService;
    @Autowired
    private SettleOrderService settleOrderService;
    /**
     * 根据结算单ID查询组合结算方式明细
     * @param settleOrderId
     * @return
     */
    @RequestMapping(value = "/listBySettleOrderId.action")
    @ResponseBody
    public BaseOutput<List<SettleWayDetail>> listBySettleOrderId(Long settleOrderId, Integer reverse) {
        if (settleOrderId == null || reverse == null) {
            return BaseOutput.failure("查询结算方式明细参数错误");
        }
        //按照作废业务逻辑，如果是冲正单，则转换为原单ID，从而查看详情
        if (Integer.valueOf(ReverseEnum.YES.getCode()).equals(reverse)) {
            settleOrderId = settleOrderService.convertReverseOrderId(settleOrderId);
        }
        return BaseOutput.success().setData(settleWayDetailService.listBySettleOrderId(settleOrderId));
    }
}
