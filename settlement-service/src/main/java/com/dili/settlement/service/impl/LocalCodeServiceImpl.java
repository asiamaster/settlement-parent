package com.dili.settlement.service.impl;

import com.dili.settlement.service.CodeService;
import com.dili.settlement.util.DateUtil;
import com.dili.settlement.util.GeneralUtil;

/**
 * 本地生成编码service 主要利用uuid转化+日期生成编码
 */
public class LocalCodeServiceImpl implements CodeService {

    //用于生成随机数的种子
    private static final String[] seeds = new String[]{"0","1","2","3","4","5","6","7","8","9"};
    private static final int len = seeds.length;

    @Override
    public String generate() {
        StringBuilder builder = new StringBuilder();
        builder.append(DateUtil.formatDateTime("yyyyMMddHHmmss"));
        random(builder);
        return builder.toString();
    }

    @Override
    public String generate(String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix);
        builder.append(DateUtil.formatDateTime("yyyyMMddHHmmss"));
        random(builder);
        return builder.toString();
    }

    /**
     * 用于生成8位随机数
     * @param builder
     */
    private void random(StringBuilder builder) {
        String uuid = GeneralUtil.uuid();
        for (int i = 0; i < 8; i++) {
            String sub = uuid.substring(i * 4, i * 4 + 4);
            builder.append(seeds[Integer.parseInt(sub, 16)%len]);
        }
    }
}
