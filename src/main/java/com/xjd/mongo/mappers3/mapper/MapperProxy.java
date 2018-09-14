package com.xjd.mongo.mappers3.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.xjd.mongo.mappers3.Mappers;

/**
 * @author elvis.xu
 * @since 2018-09-05 22:16
 */
@Slf4j
public class MapperProxy {

	public static <T> T proxy(Class<T> clazz, ThreadLocal<DefaultMapper.DefaultProperty> threadLocal) {
		if (Modifier.isFinal(clazz.getModifiers())) {
			throw new IllegalArgumentException("Cannot proxy final class:" + clazz);
		}

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		if (clazz.isInterface()) {
			enhancer.setInterfaces(new Class[]{clazz});
		}
		enhancer.setCallback(new MapperInterceptor(clazz, threadLocal));
		return createProxy(enhancer, clazz);
	}

	protected static <T> T createProxy(Enhancer enhancer, Class<T> clazz) {
		try {
			return (T) enhancer.create();
		} catch (Exception e) {
			for (Constructor<?> constructor : clazz.getConstructors()) {
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				Object[] args = new Object[parameterTypes.length];
				for (int i = 0; i < parameterTypes.length; i++) {
					args[i] = casualValueForType(parameterTypes[i]);
				}
				try {
					return (T) enhancer.create(parameterTypes, args);
				} catch (Exception t) {
					log.debug("try create instance of class '{}' using constructor({}) failed.", clazz, Arrays.toString(args), t);
				}
			}
		}
		throw new IllegalArgumentException("cannot proxy class '" + clazz + "' due to no constructor can be used to initiate it");
	}

	protected static Object casualValueForType(Class clazz) {
		if (!clazz.isPrimitive()) {
			return null;
		}
		if (char.class.equals(clazz)) {
			return ' ';
		}
		if (boolean.class.equals(clazz)) {
			return false;
		}
		// other primitives are numbers
		return Byte.valueOf((byte) 0);
	}

	protected static class MapperInterceptor implements MethodInterceptor {
		Class clazz;
		ThreadLocal<DefaultMapper.DefaultProperty> threadLocal;
		BeanMapper beanMapper;

		public MapperInterceptor(Class clazz, ThreadLocal<DefaultMapper.DefaultProperty> threadLocal) {
			this.clazz = clazz;
			this.threadLocal = threadLocal;
			this.beanMapper = Mappers.resolve(clazz);
		}

		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			if (!Modifier.isPublic(method.getModifiers())) illegalState(method);
			if (method.getParameterCount() > 0) illegalState(method);
			if (Void.class.equals(method.getReturnType())) illegalState(method);
			String name = method.getName();
			if (!name.startsWith("get") && !name.startsWith("is")) illegalState(method);
			if (name.equals("get") || name.equals("is")) illegalState(method);

			name = name.startsWith("get") ? Character.toLowerCase(name.charAt(3)) + name.substring(4)
					: Character.toLowerCase(name.charAt(2)) + name.substring(3);

			PropertyMapper propertyMapper = beanMapper.nameMap.get(name);
			if (propertyMapper == null) illegalState(method);
			DefaultMapper.DefaultProperty property = threadLocal.get();
			if (property == null) {
				property = new DefaultMapper.DefaultProperty();
				threadLocal.set(property);
			}
			property.addProp(propertyMapper);

			if (Modifier.isFinal(method.getReturnType().getModifiers())) {
				return casualValueForType(method.getReturnType());
			}
			return proxy(method.getReturnType(), threadLocal);
		}

		protected void illegalState(Method method) {
			throw new IllegalStateException("not supported operation: " + method.getName());
		}
	}
}
