package com.xjd.mongo.mappers5.core;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xjd.mongo.mappers5.Codec;
import com.xjd.mongo.mappers5.codec.*;

/**
 * @author elvis.xu
 * @since 2018-09-06 22:13
 */
public abstract class Codecs {
	protected static Map<Class, Codec> codecMap = new ConcurrentHashMap<>();
	protected static Map<Class, Codec> typeCodecMap = new LinkedHashMap<>();
	protected static LinkedList<Class> typeSortedList = new LinkedList<>();

	static {
		registryCodec(byte.class, ByteCodec.class);
		registryCodec(Byte.class, ByteCodec.class);
		registryCodec(short.class, ShortCodec.class);
		registryCodec(Short.class, ShortCodec.class);
		registryCodec(int.class, IntCodec.class);
		registryCodec(Integer.class, IntCodec.class);
		registryCodec(Object.class, BeanCodec.class);
		registryCodec(String.class, StringCodec.class);

		// fixme
	}

	public static Codec getCodec(Class<? extends Codec> clazz) {
		if (clazz == null) {
			return null;
		}
		if (Codec.class.equals(clazz)) {
			return Codec.NULL;
		}
		Codec codec = codecMap.get(clazz);
		if (codec != null) {
			return codec;
		}
		synchronized (clazz) {
			codec = codecMap.get(clazz);
			if (codec != null) {
				return codec;
			}
			codec = newCodec(clazz);
			codecMap.put(clazz, codec);
		}
		return codec;
	}

	public static Codec newCodec(Class<? extends Codec> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot substantiate codec of class '" + clazz + "'", e);
		}
	}

	public static void registryCodec(Class type, Class<? extends Codec> codecClass) {
		registryCodec(type, getCodec(codecClass));
	}

	public static void registryCodec(Class type, Codec codec) {
		typeCodecMap.put(type, codec);
		int i = 0, f = 0;
		for (Class clazz : typeSortedList) {
			if (type.isAssignableFrom(clazz)) {
				f = i + 1;
			}
			i++;
		}
		typeSortedList.add(f, type);
	}

	public static void removeCodec(Class type) {
		typeCodecMap.remove(type);
		typeSortedList.remove(type);
	}

	public static Codec resoveCodec(Class clazz, Codec codec) {
		if (codec != null && codec != Codec.NULL) return codec;
		codec = typeCodecMap.get(clazz);
		if (codec != null) return codec;

		for (Class type : typeSortedList) {
			if (type.isAssignableFrom(clazz)) {
				return typeCodecMap.get(type);
			}
		}
		return null;
	}

	public static Object encode(Object bean, Class beanClass, Codec codec) {
		codec = resoveCodec(beanClass, codec);
		if (codec == null) {
			throw new NullPointerException("Cannot find codec for type '" + beanClass + "'");
		}
		return codec.encode(bean);
	}

}
