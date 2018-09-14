package com.xjd.mongo.mappers5.annotation;

import java.lang.annotation.*;

import com.xjd.mongo.mappers5.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-05 17:26
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Prop {
	String value();
	Class<? extends Codec> codec() default Codec.class;
}
