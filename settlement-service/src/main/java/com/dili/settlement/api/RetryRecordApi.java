package com.dili.settlement.api;

import com.dili.settlement.service.RetryRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 重试记录api
 */
@RestController
@RequestMapping(value = "/api/retryRecord")
public class RetryRecordApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryRecordApi.class);

    @Resource
    private RetryRecordService retryRecordService;
    /**
     * 执行结算回调
     * @return
     */
    @RequestMapping(value = "/executeCallback")
    public void executeCallback() {
        try {
            retryRecordService.executeCallback();
        } catch (Exception e) {
            LOGGER.error("method executeCallback", e);
        }
    }
}
