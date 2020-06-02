package com.dili.settlement.api;

import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.service.SettleWayDetailService;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 查询结算方式明细
 */
@RestController
@RequestMapping(value = "/api/settleWayDetail")
public class SettleWayDetailApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleWayDetailApi.class);

    @Resource
    private SettleWayDetailService settleWayDetailService;
    /**
     * 查询结算明细列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list")
    public BaseOutput<List<SettleWayDetail>> list(@RequestBody SettleWayDetail query) {
        try {
            List<SettleWayDetail> itemList = settleWayDetailService.listByExample(query);
            return BaseOutput.success().setData(itemList);
        } catch (Exception e) {
            LOGGER.error("list", e);
            return BaseOutput.failure();
        }
    }
}
