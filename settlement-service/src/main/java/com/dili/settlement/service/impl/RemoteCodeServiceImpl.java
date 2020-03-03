package com.dili.settlement.service.impl;

import com.dili.settlement.rpc.UidRpc;
import com.dili.settlement.service.CodeService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import javax.annotation.Resource;

/**
 * 调用远程服务生成
 */
public class RemoteCodeServiceImpl implements CodeService {

    @Resource
    private UidRpc uidRpc;

    @Override
    public String generate(String type) {
        BaseOutput<String> baseOutput = uidRpc.bizNumber(type);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException("", "获取结算编号失败");
        }
        return baseOutput.getData();
    }

    @Override
    public String generate(String prefix, String type) {
        BaseOutput<String> baseOutput = uidRpc.bizNumber(type);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException("", "获取结算编号失败");
        }
        return prefix + baseOutput.getData();
    }

}
