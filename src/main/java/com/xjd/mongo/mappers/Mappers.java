package com.xjd.mongo.mappers;

import org.bson.Document;

import com.xjd.mongo.mappers.core.InnerMappers;
import com.xjd.mongo.mappers.core.InnerMockers;
import com.xjd.mongo.mappers.core.InnerResovers;
import com.xjd.mongo.mappers.core.MapperImpl;
import com.xjd.mongo.mappers.core.bean.EntityMeta;
import com.xjd.mongo.mappers4.PropContainer;

/**
 * @author elvis.xu
 * @since 2018-09-08 17:31
 */
public abstract class Mappers {
	public static <T> Mapper<T> of(Class<T> clazz) {
		return new MapperImpl<>(clazz);
	}

	public static void indent(int indent) {
		InnerMappers.indent(indent);
	}

	public static void indentReset() {
		InnerMappers.indentReset();
	}

	// encode
	public static <T> Document encode(T t, Class<T> clazz) {
		return encode(t, clazz, true);
	}

	public static <T> Document encode(T t, Class<T> clazz, boolean withNull) {
		return encode(t, clazz, null, withNull);
	}

	public static <T> Document encode(T t, Class<T> clazz, PropGen.Multi<T> includeProps, boolean withNull) {
		return InnerMappers.encode(t, clazz, withNull, includeProps, null);
	}

	public static <T> Document encode(T t, Class<T> clazz, boolean withNull, PropGen.Multi<T> excludeProps) {
		return InnerMappers.encode(t, clazz, withNull, null, excludeProps);
	}

	// decode
	public static <T> T decode(Document document, Class<T> clazz) {
		return decode(document, clazz, true);
	}

	public static <T> T decode(Document document, Class<T> clazz, boolean withNull) {
		return decode(document, clazz, null, withNull);
	}

	public static <T> T decode(Document document, Class<T> clazz, PropContainer<T> includeProps, boolean withNull) {
		return null;
	}

	public static <T> T decode(Document document, Class<T> clazz, boolean withNull, PropContainer<T> excludeProps) {
		return null;
	}

	// property
	public static String entity(Class clazz) {
		EntityMeta entityMeta = InnerResovers.getEntityMeta(clazz);
		return entityMeta == null ? null : entityMeta.resolveKey();
	}

	public static String prop(PropGen.Exec exec) {
		return InnerMappers.propKey(InnerMappers.propGen(exec, null));
	}

	public static String prop() {
		return InnerMappers.propKey(InnerMappers.prop());
	}

	public static <T> T mock(Class<T> clazz) {
		return InnerMockers.getMock(clazz, clazz, null);
	}

	// document
	public static <T> MapDocument<T> mapDocument(Class<T> clazz) {
		return mapDocument(clazz, null);
	}

	public static <T> MapDocument<T> mapDocument(Document document) {
		return mapDocument(null, document);
	}

	public static <T> MapDocument<T> mapDocument(Class<T> clazz, Document document) {
		return InnerMockers.getMapDocument(clazz, document);
	}
}
