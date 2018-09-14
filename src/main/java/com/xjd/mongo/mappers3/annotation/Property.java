package com.xjd.mongo.mappers3.annotation;

import java.lang.annotation.*;

/**
 * @author elvis.xu
 * @since 2018-09-05 17:26
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Property {
	String value();
}
