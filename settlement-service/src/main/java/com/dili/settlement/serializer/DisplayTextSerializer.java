package com.dili.settlement.serializer;

import com.dili.settlement.annotation.DisplayText;
import com.dili.settlement.component.ApplicationContextHolder;
import com.dili.ss.metadata.ValueProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

public class DisplayTextSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    //provider bean name
    private String provider;
    //后缀
    private String filedName;

    public DisplayTextSerializer(String provider, String filedName) {
        this.provider = provider;
        this.filedName = filedName;
    }

    @Override
    public void serialize(Object val, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (val == null) {
            return;
        }
        Object bean = ApplicationContextHolder.INSTANCE.getBean(this.provider);
        if (!(bean instanceof ValueProvider)) {
            return;
        }
        ValueProvider valueProvider = (ValueProvider)bean;
        jsonGenerator.writeStringField(filedName, valueProvider.getDisplayText(val, null, null));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return serializerProvider.findNullValueSerializer(null);
        }
        DisplayText displayText = beanProperty.getAnnotation(DisplayText.class);
        if (displayText == null) {
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return new DisplayTextSerializer(displayText.provider(), beanProperty.getName() + displayText.suffix());
    }
}
