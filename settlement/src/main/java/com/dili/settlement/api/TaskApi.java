package com.dili.settlement.api;

import com.dili.settlement.service.RetryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务api
 */
@RestController
@RequestMapping(value = "/api/task")
public class TaskApi {

    @Autowired
    private RetryRecordService retryRecordService;
    /**
     * 执行结算回调
     */
    @RequestMapping(value = "/executeCallback")
    public void executeCallback() {
        retryRecordService.executeCallback();
    }
}
