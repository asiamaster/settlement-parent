package com.dili.settlement.annotation;

import java.lang.annotation.*;

/**
 * 标记实体是否需要转换
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DisplayConvert {
}
