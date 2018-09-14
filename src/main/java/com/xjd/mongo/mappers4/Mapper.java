package com.xjd.mongo.mappers4;

import java.util.function.Consumer;

import org.bson.Document;

/**
 * @author elvis.xu
 * @since 2018-09-06 17:50
 */
public interface Mapper<T> {
	// encode
	Document encode(T t);

	Document encode(T t, boolean withNull);

	Document encode(T t, PropContainer<T> includeProps, boolean withNull);

	Document encode(T t, boolean withNull, PropContainer<T> excludeProps);

	// decode
	T decode(Document document);

	// property
	String prop(Consumer<T> getter);

	void propPrepare(Consumer<T> getter);

	String propPrepared();

	// document
	MappedDocument<T> doc();

	MappedDocument<T> doc(Document document);
}
