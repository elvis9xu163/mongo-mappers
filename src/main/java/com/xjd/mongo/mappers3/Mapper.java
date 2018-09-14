package com.xjd.mongo.mappers3;

/**
 * @author elvis.xu
 * @since 2018-09-05 17:15
 */
public interface Mapper<T> {
	String key(GetterInvoker<T> getterInvoker);
	String name(GetterInvoker<T> getterInvoker);
	Property property(GetterInvoker<T> getterInvoker);

	interface GetterInvoker<T> {
		void invokeGetter(T getter);
	}

	interface Property {
		String key();
		String name();
		String[] keyChain();
		String[] nameChain();
	}
}
