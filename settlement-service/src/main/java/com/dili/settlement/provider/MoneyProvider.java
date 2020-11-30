package com.dili.settlement.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 金额转换器
 */
@Component
public class MoneyProvider implements ValueProvider {
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
        return val != null ? MoneyUtils.centToYuan((Long)val) : null;
    }
}
