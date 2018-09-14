package com.xjd.mongo.mappers3.mapper;

/**
 * @author elvis.xu
 * @since 2018-09-05 21:17
 */
public class PropertyMapper {
	protected String key;
	protected String name;

	public PropertyMapper(String key, String name) {
		this.key = key;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public PropertyMapper setKey(String key) {
		this.key = key;
		return this;
	}

	public String getName() {
		return name;
	}

	public PropertyMapper setName(String name) {
		this.name = name;
		return this;
	}
}
