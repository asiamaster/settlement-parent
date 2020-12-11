package com.dili.settlement.serializer;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.dili.settlement.annotation.DisplayConvert;
import com.dili.settlement.annotation.DisplayText;
import com.dili.settlement.component.ApplicationContextHolder;
import com.dili.ss.metadata.ValueProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class DisplayTextAfterFilter extends AfterFilter {

    private static Logger LOGGER = LoggerFactory.getLogger(DisplayTextAfterFilter.class);
    @Override
    public void writeAfter(Object obj) {
        DisplayConvert displayConvert = obj.getClass().getAnnotation(DisplayConvert.class);
        if (displayConvert == null) {
            return;
        }
        Field[] fields = ReflectUtil.getFields(obj.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            DisplayText displayText = field.getAnnotation(DisplayText.class);
            if (displayText == null) {
                continue;
            }
            Object bean = ApplicationContextHolder.INSTANCE.getBean(displayText.provider());
            if (!(bean instanceof ValueProvider)) {
                return;
            }
            ValueProvider valueProvider = (ValueProvider)bean;
            String text = null;
            try {
                text = valueProvider.getDisplayText(field.get(obj), null, null);
            } catch (IllegalAccessException e) {
                LOGGER.error("通过反射获取字段值失败", e);
            }
            if (StrUtil.isNotBlank(text)) {
                super.writeKeyValue(field.getName() + displayText.suffix(), text);
            }
            field.setAccessible(false);
        }
    }
}
