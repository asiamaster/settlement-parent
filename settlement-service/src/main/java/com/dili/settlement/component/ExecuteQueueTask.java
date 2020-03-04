package com.dili.settlement.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dili.settlement.config.CallbackConfiguration;
import com.dili.settlement.domain.RetryError;
import com.dili.settlement.dto.CallbackDto;
import com.dili.settlement.enums.RetryTypeEnum;
import com.dili.settlement.service.RetryErrorService;
import com.dili.settlement.service.RetryRecordService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 用于执行回调
 */
public class ExecuteQueueTask implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteQueueTask.class);

    private String threadKey;
    private int threadId;
    private CallbackConfiguration callbackConfiguration;
    private RetryRecordService retryRecordService;
    private RetryErrorService retryErrorService;

    public ExecuteQueueTask(int threadId, CallbackConfiguration callbackConfiguration, RetryRecordService retryRecordService, RetryErrorService retryErrorService) {
        this.threadId = threadId;
        this.callbackConfiguration = callbackConfiguration;
        this.retryRecordService = retryRecordService;
        this.retryErrorService = retryErrorService;
        this.threadKey = "execute-" + this.threadId;
    }

    @Override
    public Boolean call() {
        while (true) {
            try {
                Thread.sleep(callbackConfiguration.getTaskThreadSleepMills());
            } catch (InterruptedException e) {
                LOGGER.error("execute thread sleep", e);
            }
            CallbackDto callbackDto = CallbackHolder.pollExecute();
            if (callbackDto == null) {
                continue;
            }
            try {
                post(callbackDto.getUrl(), callbackDto.getData());
                retryRecordService.delete(callbackDto.getRetryRecordId());
            } catch (Exception e) {
                LOGGER.error("execute task error", e);
                callbackDto.failure();
                CallbackHolder.offerCache(callbackDto);
                String content = e.getMessage() != null ? e.getMessage().substring(0,200) : "";
                retryErrorService.insertSelective(new RetryError(RetryTypeEnum.SETTLE_CALLBACK.getCode(), callbackDto.getBusinessId(), callbackDto.getBusinessCode(), e.getClass().getName(), content));
            }
        }
    }

    private void post(String url, Map<String, String> map) {
        HttpRequest request = HttpUtil.createPost(url);
        request.header("Content-Type", "application/json;charset=UTF-8");
        String responseBody = request.body(JSON.toJSONString(map)).execute().body();
        if (StrUtil.isBlank(responseBody)) {
            throw new BusinessException("", "回调返回内容为空");
        }
        BaseOutput<Boolean> baseOutput = JSON.parseObject(responseBody, new TypeReference<BaseOutput<Boolean>>(){}.getType());
        if (!baseOutput.isSuccess()) {
            throw new BusinessException("", baseOutput.getMessage());
        }
        if (!Boolean.TRUE.equals(baseOutput.getData())) {
            throw new BusinessException("", "业务系统返回FALSE");
        }
    }
}
