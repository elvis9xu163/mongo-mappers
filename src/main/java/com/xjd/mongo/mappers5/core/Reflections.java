package com.xjd.mongo.mappers5.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.xjd.mongo.mappers5.Codec;
import com.xjd.mongo.mappers5.annotation.Prop;
import com.xjd.mongo.mappers5.core.inner.PropertyMeta;
import com.xjd.mongo.mappers5.util.StringUtil;

/**
 * @author elvis.xu
 * @since 2018-09-06 22:13
 */
public abstract class Reflections {

	public static boolean canResolved(Class clazz) {
		if (clazz == null) {
			return false;
		}
		if (Modifier.isFinal(clazz.getModifiers())) {
			return false;
		}
		if (clazz.isArray()) {
			return false;
		}
		if (clazz.isEnum()) {
			return false;
		}
		if (clazz.isAnnotation()) {
			return false;
		}
		if (Object.class.equals(clazz)) {
			return false;
		}
		return true;
	}

	public static void checkResovled(Class clazz) {
		if (!canResolved(clazz)) {
			throw new IllegalArgumentException("Cannot resolve class '" + clazz + "'");
		}
	}

	public static <T> MapperImpl<T> resolve(Class<T> clazz) {
		checkResovled(clazz);
		MapperImpl<T> mapper = new MapperImpl<T>();

		resolveGetterAndSetter(mapper, clazz);
		resolveFileds(mapper, clazz);

		return mapper;
	}

	public static void resolveGetterAndSetter(MapperImpl mapper, Class clazz) {
		for (Method method : clazz.getMethods()) {
			if (resolveGetter(mapper, method) || resolveSetter(mapper, method)) {
				// do-nothing
			}
		}
	}

	public static boolean resolveGetter(MapperImpl mapper, Method method) {
		if (!isGetter(method)) {
			return false;
		}
		String name = method.getName();
		name = name.startsWith("get") ? Character.toLowerCase(name.charAt(3)) + name.substring(4) : Character.toLowerCase(name.charAt(2)) + name.substring(3);

		PropertyMeta propertyMeta = (PropertyMeta) mapper.propMap.get(name);
		if (propertyMeta == null) {
			propertyMeta = new PropertyMeta();
			propertyMeta.setName(name);
			mapper.propMap.put(name, propertyMeta);
			mapper.getterMap.put(method, propertyMeta);
		}
		PropertyMeta.PropertyMethod propertyMethod = new PropertyMeta.PropertyMethod().setType(method.getReturnType()).setMock(mock(method.getReturnType())).setMethod(method);
		resoveAnnotation(name, propertyMethod, method);
		propertyMeta.setGetter(propertyMethod);

		return true;
	}

	public static boolean isGetter(Method method) {
		String methodName = method.getName();
		boolean get = methodName.startsWith("get");
		boolean is = get ? false : methodName.startsWith("is");
		if (!get && !is) {
			return false;
		}
		if (get && methodName.length() < 4) {
			return false;
		}
		if (is && methodName.length() < 3) {
			return false;
		}
		if (method.getParameterCount() > 0) {
			return false;
		}
		if (isVoid(method.getReturnType())) {
			return false;
		}
		if (is && !isBoolean(method.getReturnType())) {
			return false;
		}
		if (method.getDeclaringClass().equals(Object.class)) {
			return false;
		}
		return true;
	}

	public static void resoveAnnotation(String name, PropertyMeta.PropertyMethod propertyMethod, Method method) {
		Prop annotation = method.getAnnotation(Prop.class);
		if (annotation == null) {
			return;
		}
		String key = StringUtil.trimToNull(annotation.value());
		propertyMethod.setKey(key != null ? key : name);
		propertyMethod.setCodec(Codecs.getCodec(annotation.codec()));
	}

	public static boolean resolveSetter(MapperImpl mapper, Method method) {
		if (!isSetter(method)) {
			return false;
		}

		String name = method.getName();
		name = Character.toLowerCase(name.charAt(3)) + name.substring(4);

		PropertyMeta propertyMeta = (PropertyMeta) mapper.propMap.get(name);
		if (propertyMeta == null) {
			propertyMeta = new PropertyMeta();
			propertyMeta.setName(name);
			mapper.propMap.put(name, propertyMeta);
			mapper.setterMap.put(method, propertyMeta);
		}
		PropertyMeta.PropertyMethod propertyMethod = new PropertyMeta.PropertyMethod().setType(method.getParameterTypes()[0]).setMethod(method);
		resoveAnnotation(name, propertyMethod, method);
		propertyMeta.setSetter(propertyMethod);
		return true;
	}

	public static boolean isSetter(Method method) {
		String methodName = method.getName();
		if (!methodName.startsWith("set") || methodName.length() < 4) {
			return false;
		}
		if (method.getParameterCount() != 1) {
			return false;
		}
		if (method.getDeclaringClass().equals(Object.class)) {
			return false;
		}
		return true;
	}

	public static void resolveFileds(MapperImpl mapper, Class clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			PropertyMeta propertyMeta = (PropertyMeta) mapper.propMap.get(field.getName());
			if (propertyMeta == null) continue;
			if (propertyMeta.getKey() != null) continue;
			Prop annotation = field.getAnnotation(Prop.class);
			String key = annotation != null ? StringUtil.trimToNull(annotation.value()) : null;
			propertyMeta.setKey(key != null ? key : propertyMeta.getName());
			Class<? extends Codec> codecClass = annotation != null ? annotation.codec() : Codec.class;
			propertyMeta.setCodec(Codecs.getCodec(codecClass));
		}
		Class superclass = clazz.getSuperclass();
		if (canResolved(superclass)) {
			resolveFileds(mapper, superclass);
		}
	}

	public static <E> E mock(Class<E> clazz) {
		// fixme
		return null;
	}

	public static boolean isVoid(Class clazz) {
		return void.class.equals(clazz) || Void.class.equals(clazz);
	}

	public static boolean isBoolean(Class clazz) {
		return boolean.class.equals(clazz) || Boolean.class.equals(clazz);
	}
}
