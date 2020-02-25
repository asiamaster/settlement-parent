package com.dili.settlement.api;

import com.dili.settlement.domain.ApplicationConfig;
import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.settlement.service.ApplicationConfigService;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 应用配置API
 */
@RestController
@RequestMapping(value = "/api/applicationConfig")
public class ApplicationConfigApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfigApi.class);

    @Resource
    private ApplicationConfigService applicationConfigService;

    /**
     * 查询应用配置列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list")
    public BaseOutput<List<ApplicationConfig>> list(@RequestBody ApplicationConfigDto query) {
        try {
            List<ApplicationConfig> itemList = applicationConfigService.list(query);
            return BaseOutput.success().setData(itemList);
        } catch (Exception e) {
            LOGGER.error("method list");
            return BaseOutput.failure();
        }
    }

    /**
     * 根据应用id、分组编码、编码获取值
     * @param query
     * @return
     */
    @RequestMapping(value = "/getVal")
    public BaseOutput<String> getVal(@RequestBody ApplicationConfigDto query) {
        try {
            if (query.getAppId() == null) {
                return BaseOutput.failure("应用ID为空");
            }
            if (query.getGroupCode() == null) {
                return BaseOutput.failure("分组编码为空");
            }
            if (query.getCode() == null) {
                return BaseOutput.failure("编码为空");
            }
            String val = applicationConfigService.getVal(query.getAppId(), query.getGroupCode(), query.getCode());
            return BaseOutput.success().setData(val);
        } catch (Exception e) {
            LOGGER.error("method getVal", e);
            return BaseOutput.failure();
        }
    }
}
