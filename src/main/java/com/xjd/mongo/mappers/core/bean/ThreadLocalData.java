package com.xjd.mongo.mappers.core.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author elvis.xu
 * @since 2018-09-08 13:05
 */
@Getter
@Setter
@Accessors(chain = true)
public class ThreadLocalData {
	protected int indent = 0;
	protected List<PropertyNode> propPath = new ArrayList<>();

	@Getter
	@Setter
	@Accessors(chain = true)
	public static class PropertyNode {
		protected PropertyMeta propertyMeta;
		protected Integer listIndex;
		protected Object mapKey;
	}
}
