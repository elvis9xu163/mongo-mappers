package com.xjd.mongo.mappers;

/**
 * @author elvis.xu
 * @since 2018-09-08 11:43
 */
public interface Codec<T> {

	Object encode(T source, Context context);

	T decode(Object source, Context context);

	interface Context {
		boolean withNull();

		// === property path === //
		String currentPropertyPath();

		void pushPropertyPath(String propertyName);

		void pushPropertyPath(Integer listIndex, Object mapKey);

		void popPropertyPath();

		String concatPropertyPath(String propertyName);

		boolean includePath(String path);

		// === cache === //
		void put(Object key, Object value);

		Object get(Object key);
	}
}
