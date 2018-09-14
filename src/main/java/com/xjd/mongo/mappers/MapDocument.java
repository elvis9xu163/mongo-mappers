package com.xjd.mongo.mappers;

import java.util.Date;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * @author elvis.xu
 * @since 2018-09-06 18:17
 */
public class MapDocument<E> extends Document {
	protected Document document;
	protected Class<E> clazz;

	public MapDocument(Class<E> clazz, Document delegate) {
		this.clazz = clazz;
		this.document = delegate;
	}

	public MapDocument(Class<E> clazz) {
		this(clazz, new Document());
	}

	public MapDocument() {
		this(null, new Document());
	}

	public Class<E> getBeanClass() {
		return clazz;
	}

	public Document getDocument() {
		return document;
	}

	public MapDocument<E> exec(PropGen.Exec exec) {
		return null; // 走代理
	}

	public MapDocument<E> append(PropGen<E> getter, Object value) {
		return null; // 走代理
	}

	protected String propKey(PropGen<E> getter) {
		return Mappers.of(clazz).prop(getter);
	}

	public <T> T get(PropGen<E> getter, Class<T> clazz) {
		String key = propKey(getter);
		return document.get(key, clazz);
	}

	public <T> T get(PropGen<E> getter, T defaultValue) {
		String key = propKey(getter);
		return document.get(key, defaultValue);
	}

	public Integer getInteger(PropGen<E> getter) {
		String key = propKey(getter);
		return document.getInteger(key);
	}

	public int getInteger(PropGen<E> getter, int defaultValue) {
		String key = propKey(getter);
		return document.getInteger(key, defaultValue);
	}

	public Long getLong(PropGen<E> getter) {
		String key = propKey(getter);
		return document.getLong(key);
	}

	public Double getDouble(PropGen<E> getter) {
		String key = propKey(getter);
		return document.getDouble(key);
	}

	public String getString(PropGen<E> getter) {
		String key = propKey(getter);
		return document.getString(key);
	}

	public Boolean getBoolean(PropGen<E> getter) {
		String key = propKey(getter);
		return document.getBoolean(key);
	}

	public boolean getBoolean(PropGen<E> getter, boolean defaultValue) {
		String key = propKey(getter);
		return document.getBoolean(key, defaultValue);
	}

	public ObjectId getObjectId(PropGen<E> getter) {
		String key = propKey(getter);
		return document.getObjectId(key);
	}

	public Date getDate(PropGen<E> getter) {
		String key = propKey(getter);
		return document.getDate(key);
	}

	public Object get(PropGen<E> getter) {
		String key = propKey(getter);
		return document.get(key);
	}

}
