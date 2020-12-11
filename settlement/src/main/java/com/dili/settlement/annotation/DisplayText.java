package com.dili.settlement.annotation;

import com.dili.settlement.serializer.DisplayTextSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * 用于展示文本注解 通过provider获取值
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@JacksonAnnotationsInside
@JsonSerialize(using = DisplayTextSerializer.class)
public @interface DisplayText {
    String provider();
    String suffix() default "Text";
}
