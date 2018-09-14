package com.xjd.mongo.mappers.core.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-08 12:56
 */
@Getter
@Setter
@Accessors(chain = true)
public class PropertyAnnoMeta {
	protected String key;
	protected Boolean ignored;
	protected Class<? extends Codec> codecClass;
	protected Class<?> mockClass;
	protected Class<?> componentClass;
}
