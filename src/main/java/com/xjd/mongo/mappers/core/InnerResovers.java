package com.xjd.mongo.mappers.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xjd.mongo.mappers.annotation.Entity;
import com.xjd.mongo.mappers.annotation.Property;
import com.xjd.mongo.mappers.core.assist.Reflections;
import com.xjd.mongo.mappers.core.bean.EntityMeta;
import com.xjd.mongo.mappers.core.bean.PropertyAnnoMeta;
import com.xjd.mongo.mappers.core.bean.PropertyMeta;
import com.xjd.mongo.mappers.util.StringUtil;

/**
 * @author elvis.xu
 * @since 2018-09-08 12:46
 */
public abstract class InnerResovers {
	public static final Map<Class, EntityMeta> entityMetaMap = new ConcurrentHashMap<>();

	public static EntityMeta getEntityMeta(Class<?> clazz) {
		if (clazz == null) return null;

		EntityMeta entityMeta = entityMetaMap.get(clazz);
		if (entityMeta != null) return entityMeta;

		synchronized (clazz) {
			entityMeta = entityMetaMap.get(clazz);
			if (entityMeta != null) return entityMeta;

			entityMeta = resolve(clazz);
			entityMetaMap.put(clazz, entityMeta);
		}
		return entityMeta;
	}

	public static EntityMeta resolve(Class<?> clazz) {
		EntityMeta entityMeta = new EntityMeta();
		entityMeta.setClazz(clazz);
		if (Object.class.equals(clazz)) return entityMeta; // 优化, 如果是Object不用解析了
		resolveClass(clazz, entityMeta);
		resolveMethods(clazz, entityMeta);
		resolveFields(clazz, entityMeta);
		return entityMeta;
	}

	protected static void resolveClass(Class<?> clazz, EntityMeta entityMeta) {
		Entity annotation = clazz.getAnnotation(Entity.class);
		if (annotation != null) {
			entityMeta.setKey(StringUtil.trim(annotation.value()));
			entityMeta.setCodecClass(annotation.codec());
			entityMeta.setMockClass(annotation.mock());
		}
	}

	protected static void resolveMethods(Class<?> clazz, EntityMeta entityMeta) {
		for (Method method : clazz.getMethods()) { // only for public methods
			boolean isGet = Reflections.isGetter(method);
			boolean isSet = !isGet && Reflections.isSetter(method);
			if (isGet || isSet) {
				PropertyMeta propertyMeta = getAndCreatePropertyMeta(entityMeta, Reflections.resolvePropertyName(method));
				PropertyMeta.MethodMeta methodMeta = new PropertyMeta.MethodMeta();
				if (isGet) propertyMeta.setGetterMeta(methodMeta);
				else propertyMeta.setSetterMeta(methodMeta);

				resolveGetterOrSetter(method, isGet, methodMeta);
			}
		}
	}

	protected static PropertyMeta getAndCreatePropertyMeta(EntityMeta entityMeta, String propertyName) {
		PropertyMeta propertyMeta = entityMeta.getPropertyMetaMap().get(propertyName);
		if (propertyMeta == null) {
			propertyMeta = new PropertyMeta();
			propertyMeta.setName(propertyName);
			entityMeta.getPropertyMetaMap().put(propertyName, propertyMeta);
		}
		return propertyMeta;
	}

	protected static void resolveGetterOrSetter(Method method, boolean isGetter, PropertyMeta.MethodMeta methodMeta) {
		methodMeta.setMethod(method);
		if (isGetter) methodMeta.setType(method.getReturnType());
		else methodMeta.setType(method.getParameterTypes()[0]);
		resolvePropertyAnnotation(method.getAnnotation(Property.class), methodMeta);
	}

	protected static void resolvePropertyAnnotation(Property annotation, PropertyAnnoMeta meta) {
		if (annotation == null) return;
		if (meta.getKey() != null) return; // 在子类中已解析到了，不能覆盖子类的设置
		meta.setKey(StringUtil.trim(annotation.value()));
		meta.setIgnored(annotation.ignored());
		meta.setCodecClass(annotation.codec());
		meta.setMockClass(annotation.mock());
		meta.setComponentClass(annotation.componentClass());
	}

	protected static void resolveFields(Class<?> clazz, EntityMeta entityMeta) {
		for (Field field : clazz.getDeclaredFields()) { // all fields
			PropertyMeta propertyMeta = entityMeta.getPropertyMetaMap().get(field.getName());
			if (propertyMeta == null) continue; //没有getter或setter的field忽略
			resolveField(field, propertyMeta);
		}
		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null && !superclass.equals(Object.class)) { // 递归解析, 注意不要覆盖已经解析到了的
			resolveFields(superclass, entityMeta);
		}
	}

	protected static void resolveField(Field field, PropertyMeta meta) {
		resolvePropertyAnnotation(field.getAnnotation(Property.class), meta);
	}

}
