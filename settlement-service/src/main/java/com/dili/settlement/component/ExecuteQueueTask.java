package com.dili.settlement.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dili.settlement.dto.CallbackDto;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 用于执行回调
 */
public class ExecuteQueueTask implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteQueueTask.class);
    @Override
    public Boolean call() {
        while (true) {
            CallbackDto callbackDto = CallbackHolder.pollExecute();
            if (callbackDto == null) {
                continue;
            }
            boolean result = post(callbackDto.getUrl(), callbackDto.getData());
            if (!result) {
                callbackDto.failure();
                CallbackHolder.offerCache(callbackDto);
            }
        }
    }

    private boolean post(String url, Map<String, String> map) {
        HttpRequest request = HttpUtil.createPost(url);
        request.header("Content-Type", "application/json;charset=UTF-8");
        String responseBody = request.body(JSON.toJSONString(map)).execute().body();
        if (StrUtil.isBlank(responseBody)) {
            LOGGER.error("POST RESPONSE BODY IS EMPTY");
            return false;
        }
        BaseOutput<Boolean> baseOutput = JSON.parseObject(responseBody, new TypeReference<BaseOutput<Boolean>>(){}.getType());
        if (!baseOutput.isSuccess()) {
            LOGGER.error("POST EXECUTE FAILURE");
            return false;
        }
        return baseOutput.getData();
    }
}
