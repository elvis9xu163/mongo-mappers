package com.xjd.mongo.mappers.annotation;

import java.lang.annotation.*;

import com.xjd.mongo.mappers.Codec;


/**
 * @author elvis.xu
 * @since 2018-09-05 17:26
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Entity {
	String value();
	Class<? extends Codec> codec() default Codec.class;
	Class<?> mock() default Void.class;
}
