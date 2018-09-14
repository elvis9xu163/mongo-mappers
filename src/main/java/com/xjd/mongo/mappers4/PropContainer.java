package com.xjd.mongo.mappers4;

import java.util.function.BiConsumer;

/**
 * @author elvis.xu
 * @since 2018-09-06 18:03
 */
public interface PropContainer<T> extends BiConsumer<T, PropContainer.Container> {

	interface Container {
		Container add(Object getterCall);
	}
}
