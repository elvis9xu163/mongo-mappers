package com.xjd.mongo.mappers5;

import java.util.function.Consumer;

import org.bson.Document;

import com.xjd.mongo.mappers5.core.Mappers;
import com.xjd.mongo.mappers4.MappedDocument;
import com.xjd.mongo.mappers4.PropContainer;

/**
 * @author elvis.xu
 * @since 2018-09-06 17:50
 */
public interface Mapper<T> {

	static <E>  Mapper<E> of(Class<E> clazz) {
		return Mappers.of(clazz);
	}

	static void indent(int indent) {
		Mappers.indent(indent);
	}

	static void indentReset() {
		Mappers.indentReset();
	}

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
