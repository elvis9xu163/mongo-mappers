package com.xjd.mongo.mappers5.core.inner;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author elvis.xu
 * @since 2018-09-06 22:45
 */
@Getter
@Setter
@Accessors(chain = true)
public class PropertyPath {
	protected List<PropertyNode> nodes = new ArrayList<>();
	protected int indent = 0;
}
