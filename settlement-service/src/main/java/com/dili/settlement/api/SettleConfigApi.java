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

    /**
     * 查询结算配置列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list")
    public BaseOutput<List<SettleConfig>> list(@RequestBody SettleConfig query) {
        try {
            if (query.getMarketId() == null) {
                return BaseOutput.failure("市场ID为空");
            }
            List<SettleConfig> itemList = settleConfigService.list(query);
            return BaseOutput.success().setData(itemList);
        } catch (Exception e) {
            LOGGER.error("method list");
            return BaseOutput.failure();
        }
    }

    /**
     * 根据分组编码、编码获取值
     * @param query
     * @return
     */
    @RequestMapping(value = "/getVal")
    public BaseOutput<String> getVal(@RequestBody SettleConfig query) {
        try {
            if (query.getMarketId() == null) {
                return BaseOutput.failure("市场ID为空");
            }
            if (query.getGroupCode() == null) {
                return BaseOutput.failure("分组编码为空");
            }
            if (query.getCode() == null) {
                return BaseOutput.failure("编码为空");
            }
            String val = settleConfigService.getVal(query.getGroupCode(), query.getCode());
            return BaseOutput.success().setData(val);
        } catch (Exception e) {
            LOGGER.error("method getVal", e);
            return BaseOutput.failure();
        }
    }
}
