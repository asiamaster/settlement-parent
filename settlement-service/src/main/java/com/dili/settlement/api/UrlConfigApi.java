package com.dili.settlement.api;

import com.dili.settlement.domain.UrlConfig;
import com.dili.settlement.service.UrlConfigService;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 路径配置api
 */
@RestController
@RequestMapping(value = "/api/urlConfig")
public class UrlConfigApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlConfigApi.class);

    @Resource
    private UrlConfigService urlConfigService;
    /**
     * 根据业务类型 和 路径类型 获取访问路径
     * @param query
     * @return
     */
    @RequestMapping(value = "/getUrl")
    public BaseOutput<UrlConfig> getUrl(@RequestBody UrlConfig query) {
        try {
            if (query.getBusinessType() == null) {
                return BaseOutput.failure("业务类型为空");
            }
            if (query.getType() == null) {
                return BaseOutput.failure("路径类型为空");
            }
            String url = urlConfigService.getUrl(query.getBusinessType(), query.getType());
            return BaseOutput.success().setData(url);
        } catch (Exception e) {
            LOGGER.error("method getUrl", e);
            return BaseOutput.failure();
        }
    }
}
