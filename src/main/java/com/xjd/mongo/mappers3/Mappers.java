package com.xjd.mongo.mappers3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xjd.mongo.mappers3.mapper.BeanMapper;
import com.xjd.mongo.mappers3.mapper.DefaultMapper;

/**
 * @author elvis.xu
 * @since 2018-09-05 17:15
 */
public class Mappers {
	protected static Map<Class, BeanMapper> beanMapperMap = new ConcurrentHashMap<>();
	protected static Map<Class, Mapper> mapperMap = new ConcurrentHashMap<>();

	public static <T> Mapper<T> of(Class<T> beanClass) {
		if (beanClass == null) {
			return null;
		}

		Mapper<T> mapper = mapperMap.get(beanClass);
		if (mapper != null) {
			return mapper;
		}

		synchronized (beanClass) {
			mapper = mapperMap.get(beanClass);
			if (mapper != null) {
				return mapper;
			}

			mapper = new DefaultMapper<>(beanClass);
			mapperMap.put(beanClass, mapper);
		}
		return mapper;
	}

	public static BeanMapper resolve(Class beanClass) {
		if (beanClass == null) {
			return null;
		}

		BeanMapper mapper = beanMapperMap.get(beanClass);
		if (mapper != null) {
			return mapper;
		}

		synchronized (beanClass) {
			mapper = beanMapperMap.get(beanClass);
			if (mapper != null) {
				return mapper;
			}

			mapper = new BeanMapper(beanClass);
			beanMapperMap.put(beanClass, mapper);
		}
		return mapper;
	}

}
