package com.dili.settlement.api;

import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.service.SettleConfigService;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 结算配置相关api
 */
@RestController
@RequestMapping(value = "/api/settleConfig")
public class SettleConfigApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleConfigApi.class);

    @Resource
    private SettleConfigService settleConfigService;

    @RequestMapping(value = "/list")
    public BaseOutput<List<SettleConfig>> list(@RequestBody SettleConfig query) {
        try {
            List<SettleConfig> itemList = settleConfigService.listByExample(query);
            return BaseOutput.success().setData(itemList);
        } catch (Exception e) {
            LOGGER.error("method list");
            return BaseOutput.failure();
        }
    }
}
