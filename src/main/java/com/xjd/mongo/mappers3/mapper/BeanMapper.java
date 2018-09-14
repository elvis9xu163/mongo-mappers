package com.xjd.mongo.mappers3.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.xjd.mongo.mappers3.annotation.Property;
import com.xjd.mongo.mappers3.util.StringUtil;

/**
 * @author elvis.xu
 * @since 2018-09-05 21:15
 */
public class BeanMapper {
	protected Class beanClass;
	protected Map<String, PropertyMapper> keyMap = new HashMap<>();
	protected Map<String, PropertyMapper> nameMap = new HashMap<>();

	public BeanMapper(Class beanClass) {
		this.beanClass = beanClass;
		resolve(beanClass);
	}

	protected void resolve(Class clazz) {
		for (Method method : clazz.getDeclaredMethods()) {
			if (!Modifier.isPublic(method.getModifiers())) continue;
			if (method.getParameterCount() > 0) continue;
			if (Void.class.equals(method.getReturnType())) continue;
			String name = method.getName();
			if (!name.startsWith("get") && !name.startsWith("is")) continue;
			if (name.equals("get") || name.equals("is")) continue;

			name = name.startsWith("get") ? Character.toLowerCase(name.charAt(3)) + name.substring(4)
					: Character.toLowerCase(name.charAt(2)) + name.substring(3);

			String key = name;
			Property annotation = method.getAnnotation(Property.class);
			if (annotation != null) {
				if (StringUtil.trimToNull(annotation.value()) != null) {
					key = StringUtil.trimToNull(annotation.value());
				}
			} else {
				String tmpKey = resoveFromField(beanClass, name);
				if (tmpKey != null) {
					key = tmpKey;
				}
			}
			PropertyMapper propertyMapper = new PropertyMapper(key, name);
			keyMap.put(key, propertyMapper);
			nameMap.put(name, propertyMapper);
		}

		Class superclass = clazz.getSuperclass();
		if (superclass != null && !superclass.equals(Object.class)) {
			resolve(superclass);
		}
	}

	protected String resoveFromField(Class beanClass, String name) {
		try {
			Field field = beanClass.getDeclaredField(name);
			Property annotation = field.getAnnotation(Property.class);
			if (annotation != null && StringUtil.trimToNull(annotation.value()) != null) {
				return StringUtil.trimToNull(annotation.value());
			}
		} catch (NoSuchFieldException e) {
			//
		}
		return null;
	}

}
