package com.xjd.mongo.mappers;

import org.bson.Document;

/**
 * @author elvis.xu
 * @since 2018-09-08 11:43
 */
public interface Mapper<T> {
	// encode
	Document encode(T t);

	Document encode(T t, boolean withNull);

	Document encode(T t, PropGen.Multi<T> includeProps, boolean withNull);

	Document encode(T t, boolean withNull, PropGen.Multi<T> excludeProps);

	// decode
	T decode(Document document);

	T decode(Document document, boolean withNull);

	T decode(Document document, PropGen.Multi<T> includeProps, boolean withNull);

	T decode(Document document, boolean withNull, PropGen.Multi<T> excludeProps);

	// property
	String entity();

	String prop(PropGen<T> propGen);

	// document
	MapDocument<T> doc();

	MapDocument<T> doc(Document document);
}
