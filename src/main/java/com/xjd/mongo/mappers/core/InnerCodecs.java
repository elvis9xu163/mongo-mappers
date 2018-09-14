package com.xjd.mongo.mappers.core;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.xjd.mongo.mappers.Codec;
import com.xjd.mongo.mappers.codec.*;
import com.xjd.mongo.mappers.core.bean.EntityMeta;
import com.xjd.mongo.mappers.core.bean.ThreadLocalData;


/**
 * @author elvis.xu
 * @since 2018-09-08 11:41
 */
public abstract class InnerCodecs {
	protected static Map<Class, Codec> codecMap = new ConcurrentHashMap<>(); // 后续还会有并发加入

	protected static Map<Class, Codec> typeCodecMap = new LinkedHashMap<>(); // 在初始化阶段就已经加入
	protected static LinkedList<Class> typeSortedList = new LinkedList<>();

	// ==== codec management ===
	static {
		// 基本
		registerCodec(void.class, VoidCodec.class);
		registerCodec(Void.class, VoidCodec.class);
		registerCodec(Object.class, ObjectCodec.class);
		registerCodec(byte.class, ByteCodec.class);
		registerCodec(Byte.class, ByteCodec.class);
		registerCodec(short.class, ShortCodec.class);
		registerCodec(Short.class, ShortCodec.class);
		registerCodec(int.class, IntCodec.class);
		registerCodec(Integer.class, IntCodec.class);
		registerCodec(long.class, LongCodec.class);
		registerCodec(Long.class, LongCodec.class);
		registerCodec(float.class, FloatCodec.class);
		registerCodec(Float.class, FloatCodec.class);
		registerCodec(double.class, DoubleCodec.class);
		registerCodec(Double.class, DoubleCodec.class);
		registerCodec(char.class, CharCodec.class);
		registerCodec(Character.class, CharCodec.class);
		registerCodec(boolean.class, BooleanCodec.class);
		registerCodec(BooleanCodec.class, BooleanCodec.class);
		registerCodec(String.class, StringCodec.class);

		// 集合数组
		registerCodec(Collection.class, CollectionCodec.class);
		registerCodec(Map.class, MapCodec.class);
		registerCodec(Object[].class, ObjectArrayCodec.class);
		registerCodec(byte[].class, PrimitiveArrayCodec.class);
		registerCodec(short[].class, PrimitiveArrayCodec.class);
		registerCodec(int[].class, PrimitiveArrayCodec.class);
		registerCodec(long[].class, PrimitiveArrayCodec.class);
		registerCodec(float[].class, PrimitiveArrayCodec.class);
		registerCodec(double[].class, PrimitiveArrayCodec.class);
		registerCodec(char[].class, PrimitiveArrayCodec.class);
		registerCodec(boolean[].class, PrimitiveArrayCodec.class);

		// mongo的一些 Point...
		registerCodec(Bson.class, BsonCodec.class);
		registerCodec(ObjectId.class, ObjectIdCodec.class);
	}

	public static Codec getCodec(Class<? extends Codec> clazz) {
		if (clazz == null) return null;
		if (Codec.class.equals(clazz)) return null;

		Codec codec = codecMap.get(clazz);
		if (codec != null) return codec;

		synchronized (clazz) {
			codec = codecMap.get(clazz);
			if (codec != null) return codec;

			codec = createCodec(clazz);
			codecMap.put(clazz, codec);
		}
		return codec;
	}

	public static Codec createCodec(Class<? extends Codec> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot instantiate codec of class '" + clazz + "'", e);
		}
	}

	public static void registerCodec(Class type, Class<? extends Codec> codecClass) {
		Codec codec = getCodec(codecClass);
		if (codec == null) throw new IllegalArgumentException("Cannot get codec instance for '" + codecClass + "'");
		registerCodec(type, codec);
	}

	public static void registerCodec(Class type, Codec codec) {
		if (type == null) throw new IllegalArgumentException("type cannot be null");
		if (codec == null) throw new IllegalArgumentException("codec cannot be null");
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

	public static void unregisterCodec(Class type) {
		typeCodecMap.remove(type);
		typeSortedList.remove(type);
	}

	public static Codec resolveCodec(Class type, Class<? extends Codec> codecClass) {
		// 使用指定的codec
		Codec codec = getCodec(codecClass);
		if (codec != null) return codec;

		// 使用类型配置的codec
		EntityMeta entityMeta = InnerResovers.getEntityMeta(type);
		if (entityMeta != null) {
			codec = getCodec(entityMeta.getCodecClass());
			if (codec != null) return codec;
		}

		// 使用预配置的codec
		if (type == null) {
			type = Object.class;
		}

		codec = typeCodecMap.get(type);
		if (codec != null) return codec;

		// 顺序查找支持的Codec
		for (Class t : typeSortedList) {
			if (t.isAssignableFrom(type)) {
				return typeCodecMap.get(t);
			}
		}

		return null;
	}


	// ==== codec management ===
	public static Object encode(Object source, Class sourceClass, Class<? extends Codec> codecClass, Codec.Context context) {
		Class type = sourceClass != null ? sourceClass : (source != null ? source.getClass() : null);
		Codec codec = resolveCodec(type, codecClass);
		if (codec == null) throw new IllegalStateException("Cannot find codec for '" + type + "' with codec class '" + codecClass + "'");
		if (context == null) context = createCodecContext(true, null, null);
		return codec.encode(source, context);
	}

	protected static CodecContext createCodecContext(boolean withNull, List<List<ThreadLocalData.PropertyNode>> includeProps, List<List<ThreadLocalData.PropertyNode>> excludeProps) { // FIXME 其它的一些参数
		return new CodecContext(withNull, includeProps, excludeProps);
	}
}
