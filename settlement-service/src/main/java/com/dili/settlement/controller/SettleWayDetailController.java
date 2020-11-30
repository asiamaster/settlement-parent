package com.dili.settlement.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleWayDetail;
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
    /**
     * 根绝结算单编号查询结算方式明细列表
     * @param settleCode
     * @return
     */
    @RequestMapping(value = "/listBySettleCode.action")
    @ResponseBody
    public BaseOutput<List<SettleWayDetail>> listBySettleCode(String settleCode) {
        if (StrUtil.isBlank(settleCode)) {
            return BaseOutput.failure("结算单号为空");
        }
        SettleWayDetail query = new SettleWayDetail();
        query.setSettleOrderCode(settleCode);
        List<SettleWayDetail> itemList = settleWayDetailService.listByExample(query);
        return BaseOutput.success().setData(itemList);
    }
}
