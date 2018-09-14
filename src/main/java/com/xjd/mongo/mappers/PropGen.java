package com.xjd.mongo.mappers;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author elvis.xu
 * @since 2018-09-08 17:50
 */
public interface PropGen<T> extends Consumer<T> {

	public static interface Exec extends PropGen {
		@Override
		default void accept(Object o) {
			accept();
		}

		void accept();
	}

	public static interface Multi<T> extends BiConsumer<T, MultiCollector> {
	}

	public static interface MultiCollector {
		void add(Object any);
	}
}
