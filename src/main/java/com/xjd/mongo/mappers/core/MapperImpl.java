package com.xjd.mongo.mappers.core;

import org.bson.Document;

import com.xjd.mongo.mappers.MapDocument;
import com.xjd.mongo.mappers.Mapper;
import com.xjd.mongo.mappers.Mappers;
import com.xjd.mongo.mappers.PropGen;

/**
 * @author elvis.xu
 * @since 2018-09-08 17:18
 */
public class MapperImpl<T> implements Mapper<T> {
	protected Class<T> clazz;

	public MapperImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Document encode(T t) {
		return Mappers.encode(t, clazz);
	}

	@Override
	public Document encode(T t, boolean withNull) {
		return Mappers.encode(t, clazz, withNull);
	}

	@Override
	public Document encode(T t, PropGen.Multi<T> includeProps, boolean withNull) {
		return Mappers.encode(t, clazz, includeProps, withNull);
	}

	@Override
	public Document encode(T t, boolean withNull, PropGen.Multi<T> excludeProps) {
		return Mappers.encode(t, clazz, withNull, excludeProps);
	}

	@Override
	public T decode(Document document) {
		return null;
	}

	@Override
	public T decode(Document document, boolean withNull) {
		return null;
	}

	@Override
	public T decode(Document document, PropGen.Multi<T> includeProps, boolean withNull) {
		return null;
	}

	@Override
	public T decode(Document document, boolean withNull, PropGen.Multi<T> excludeProps) {
		return null;
	}

	@Override
	public String entity() {
		return Mappers.entity(clazz);
	}

	@Override
	public String prop(PropGen<T> propGen) {
		return InnerMappers.propKey(InnerMappers.propGen(propGen, clazz));
	}

	@Override
	public MapDocument<T> doc() {
		return Mappers.mapDocument(clazz);
	}

	@Override
	public MapDocument<T> doc(Document document) {
		return Mappers.mapDocument(document);
	}
}
