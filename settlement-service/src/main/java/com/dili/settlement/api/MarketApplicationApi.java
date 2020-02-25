package com.dili.settlement.api;

import com.dili.settlement.domain.MarketApplication;
import com.dili.settlement.service.MarketApplicationService;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 市场应用API
 */
@RestController
@RequestMapping(value = "/api/marketApplication")
public class MarketApplicationApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketApplicationApi.class);

    @Resource
    private MarketApplicationService marketApplicationService;

    /**
     * 查询列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list")
    public BaseOutput<List<MarketApplication>> list(@RequestBody MarketApplication query) {
        try {
            List<MarketApplication> itemList = marketApplicationService.listByExample(query);
            return BaseOutput.success().setData(itemList);
        } catch (Exception e) {
            LOGGER.error("method list", e);
            return BaseOutput.failure();
        }
    }
}
