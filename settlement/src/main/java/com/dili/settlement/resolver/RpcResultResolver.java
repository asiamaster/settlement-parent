package com.dili.settlement.resolver;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc 结果解析器
 */
public class RpcResultResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcResultResolver.class);

    /**
     * 判断baseOutput.isSuccess()为false则抛出异常，并返回远程服务原始错误码及提示信息,目前错误码都是200为成功，非200则失败
     * @param <T>
     * @param baseOutput
     * @param serviceName 服务名或功能用于日志记录
     * @return
     */
    public static <T> T resolver(BaseOutput<T> baseOutput, String serviceName) {
        if (!baseOutput.isSuccess()) {
            LOGGER.error("{}远程服务返回了一个错误![{}]", serviceName, JSONObject.toJSONString(baseOutput));
            throw new BusinessException(baseOutput.getCode(), baseOutput.getMessage());
        }
        return baseOutput.getData();
    }
}
