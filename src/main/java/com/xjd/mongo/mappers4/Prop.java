package com.xjd.mongo.mappers4;

import java.lang.annotation.*;

/**
 * @author elvis.xu
 * @since 2018-09-06 19:22
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Prop {
	String value();
}
