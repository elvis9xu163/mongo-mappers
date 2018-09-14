package com.xjd.mongo.mappers.core.bean;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-08 12:48
 */
@Getter
@Setter
@Accessors(chain = true)
public class EntityMeta {
	protected Class clazz;
	protected String key;
	protected Class<? extends Codec> codecClass;
	protected Class<?> mockClass;
	protected Map<String, PropertyMeta> propertyMetaMap = new HashMap<>();

	public String resolveKey() {
		return key != null && !"".equals(key) ? key : clazz.getSimpleName();
	}
}
