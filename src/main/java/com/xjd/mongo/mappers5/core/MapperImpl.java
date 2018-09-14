package com.xjd.mongo.mappers5.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import lombok.Getter;

import org.bson.Document;

import com.xjd.mongo.mappers5.Mapper;
import com.xjd.mongo.mappers5.core.inner.PropertyMeta;
import com.xjd.mongo.mappers4.MappedDocument;
import com.xjd.mongo.mappers4.PropContainer;

/**
 * @author elvis.xu
 * @since 2018-09-06 22:18
 */

@Getter
public class MapperImpl<T> implements Mapper<T> {
	protected Map<Method, PropertyMeta> getterMap = new HashMap<>();
	protected Map<Method, PropertyMeta> setterMap = new HashMap<>();
	protected Map<String, PropertyMeta> propMap = new HashMap<>();


	protected Document encode(T t, boolean withNull, PropContainer<T> includeProps, PropContainer<T> excludeProps) {
		if (t == null) return null;
		return (Document) Codecs.encode(t, t.getClass(), null);
	}

	@Override
	public Document encode(T t) {
		return encode(t, true);
	}

	@Override
	public Document encode(T t, boolean withNull) {
		return encode(t, null, withNull);
	}

	@Override
	public Document encode(T t, PropContainer<T> includeProps, boolean withNull) {
		return encode(t, withNull, includeProps, null);
	}

	@Override
	public Document encode(T t, boolean withNull, PropContainer<T> excludeProps) {
		return encode(t, withNull, null, excludeProps);
	}

	@Override
	public T decode(Document document) {
		return null;
	}

	@Override
	public String prop(Consumer<T> getter) {
		return null;
	}

	@Override
	public void propPrepare(Consumer<T> getter) {

	}

	@Override
	public String propPrepared() {
		return null;
	}

	@Override
	public MappedDocument<T> doc() {
		return null;
	}

	@Override
	public MappedDocument<T> doc(Document document) {
		return null;
	}
}
