package com.dili.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.handler.ServiceNameHolder;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.service.DictionaryService;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典表相关实现类
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DataDictionaryRpc dataDictionaryRpc;

    @Override
    public String getSingleDictVal(String code, Long firmId) {
        return this.getSingleDictVal(code, firmId, null);
    }

    @Override
    public String getSingleDictVal(String code, Long firmId, String defaultVal) {
        List<DataDictionaryValue> items = RpcResultResolver.resolver(dataDictionaryRpc.listDataDictionaryValueByDdCode(code), ServiceNameHolder.UAP_SERVICE_NAME);
        if (CollUtil.isEmpty(items)) {
            return defaultVal;
        }
        List<DataDictionaryValue> target = items.stream().filter(temp -> {
            return Integer.valueOf(1).equals(temp.getState()) && firmId.equals(temp.getFirmId());
        }).collect(Collectors.toList());
        if (CollUtil.isEmpty(target)) {
            return defaultVal;
        }
        return target.get(0).getCode();
    }
}
