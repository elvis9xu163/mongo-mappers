package com.xjd.mongo.mappers5.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xjd.mongo.mappers5.core.inner.PropertyPath;

/**
 * @author elvis.xu
 * @since 2018-09-06 17:50
 */
public abstract class Mappers {
	static Map<Class, MapperImpl> mapperMap = new ConcurrentHashMap<>();
	static ThreadLocal<PropertyPath> propPathThreadLocal = new ThreadLocal<>();

	public static <T> MapperImpl<T> of(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}

		MapperImpl<T> mapper = mapperMap.get(clazz);
		if (mapper != null) {
			return mapper;
		}

		synchronized (clazz) {
			mapper = mapperMap.get(clazz);
			if (mapper != null) {
				return mapper;
			}

			mapper = Reflections.resolve(clazz);
			mapperMap.put(clazz, mapper);
		}
		return mapper;
	}

	public static void indent(int indent) {
		createPropPathIfAbsent().setIndent(indent);
	}

	public static void indentReset() {
		createPropPathIfAbsent().setIndent(0);
	}

	protected static PropertyPath getPropPath() {
		return propPathThreadLocal.get();
	}

	protected static PropertyPath createPropPathIfAbsent() {
		PropertyPath path = propPathThreadLocal.get();
		if (path == null) {
			path = new PropertyPath();
			propPathThreadLocal.set(path);
		}
		return path;
	}

}
