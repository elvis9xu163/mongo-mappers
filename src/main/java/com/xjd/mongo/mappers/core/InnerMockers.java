package com.xjd.mongo.mappers.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.bson.Document;

import com.xjd.mongo.mappers.MapDocument;
import com.xjd.mongo.mappers.PropGen;
import com.xjd.mongo.mappers.core.assist.Reflections;
import com.xjd.mongo.mappers.core.bean.EntityMeta;
import com.xjd.mongo.mappers.core.bean.PropertyMeta;
import com.xjd.mongo.mappers.core.bean.ThreadLocalData;

/**
 * @author elvis.xu
 * @since 2018-09-08 14:43
 */
@Slf4j
public abstract class InnerMockers {
	protected static Map<Class, Object> mockMap = new ConcurrentHashMap<>();

	public static <T> T getMock(Class<? extends T> clazz, Class<T> targetClass, Class<?> componentClass) {
		if (clazz == null) return null;

		if (!canMock(clazz)) return nonMock(clazz);

		if (List.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz)) {
			// list 和 map 不缓存
			return mock(clazz, targetClass, componentClass);
		}

		T mock = (T) mockMap.get(clazz);
		if (mock != null) return mock;
		synchronized (clazz) {
			mock = (T) mockMap.get(clazz);
			if (mock != null) return mock;

			mock = mock(clazz, targetClass, componentClass);
			mockMap.put(clazz, mock);
		}
		return mock;
	}

	public static <T> MapDocument<T> getMapDocument(Class<T> clazz, Document document) {
		if (document == null) document = new Document();
		else if (document instanceof MapDocument) document = ((MapDocument) document).getDocument();

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(MapDocument.class);
		enhancer.setCallback(new DocumentInterceptor(new MapDocument<T>(clazz, document)));
		return createProxy(enhancer, MapDocument.class);
	}

	protected static boolean canMock(Class clazz) {
		// Object 也不再做代理,代理没有意义
		return Modifier.isFinal(clazz.getModifiers()) || clazz.isAnnotation() || clazz.equals(Object.class) ? false : true;
	}

	protected static <T> T nonMock(Class<T> clazz) {
		if (clazz.isPrimitive()) {
			if (void.class.equals(clazz)) return null;
			if (boolean.class.equals(clazz)) return (T) Boolean.FALSE;
			if (char.class.equals(clazz)) return (T) Character.valueOf(' ');
			if (byte.class.equals(clazz)) return (T) Byte.valueOf((byte) 0);
			if (short.class.equals(clazz)) return (T) Short.valueOf((byte) 0);
			if (int.class.equals(clazz)) return (T) Integer.valueOf((byte) 0);
			if (long.class.equals(clazz)) return (T) Long.valueOf((byte) 0);
			if (float.class.equals(clazz)) return (T) Float.valueOf((byte) 0);
			if (double.class.equals(clazz)) return (T) Double.valueOf((byte) 0);
		}
		return null;
	}

	protected static <T> T mock(Class<? extends T> clazz, Class<T> targetClass, Class<?> componentClass) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		if (clazz.isInterface()) {
			enhancer.setInterfaces(new Class[]{clazz});
		}
		enhancer.setCallback(new MockInterceptor(targetClass, componentClass));
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
					args[i] = nonMock(parameterTypes[i]);
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


	protected static class MockInterceptor implements MethodInterceptor {
		Class targetClass;
		Class componentClass;

		public MockInterceptor(Class targetClass, Class componentClass) {
			this.targetClass = targetClass;
			this.componentClass = componentClass;
		}

		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			boolean isListGet = Reflections.isListGet(method);
			boolean isMapGet = !isListGet && Reflections.isMapGet(method);
			boolean isGetter = !isListGet && !isMapGet && Reflections.isGetter(method);

			if (!isListGet && !isMapGet && !isGetter) illegalState(method);

			ThreadLocalData.PropertyNode propertyNode = new ThreadLocalData.PropertyNode();
			Class mockClass = null, componentClass = null, returnType = null;
			if (isListGet) {
				propertyNode.setListIndex((Integer) args[0]);
				mockClass = this.componentClass;
				returnType = mockClass != null ? mockClass : method.getReturnType();

			} else if (isMapGet) {
				propertyNode.setMapKey(args[0]);
				mockClass = this.componentClass;
				returnType = mockClass != null ? mockClass : method.getReturnType();

			} else {
				EntityMeta entityMeta = InnerResovers.getEntityMeta(targetClass);
				if (entityMeta == null) throw new IllegalStateException("Cannot get entity meta for '" + targetClass + "'");
				PropertyMeta propertyMeta = entityMeta.getPropertyMetaMap().get(Reflections.resolvePropertyName(method));
				if (propertyMeta == null) illegalState(method);
				propertyNode = new ThreadLocalData.PropertyNode();
				propertyNode.setPropertyMeta(propertyMeta);
				mockClass = propertyMeta.resolveMockClass(true);
				componentClass = propertyMeta.resolveComponentClass(true);
				returnType = method.getReturnType();
			}
			InnerMappers.propAdd(propertyNode);


			if (mockClass == null || Reflections.isVoid(mockClass)) mockClass = returnType;
			return getMock(mockClass, returnType, componentClass);
		}

		protected void illegalState(Method method) {
			throw new IllegalStateException("The mock only support invoke getter of target bean, not supported operation: " + method.getName());
		}
	}

	protected static class DocumentInterceptor implements MethodInterceptor {
		MapDocument target;

		public DocumentInterceptor(MapDocument target) {
			this.target = target;
		}

		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			Class<?> declaringClass = method.getDeclaringClass();
			String name = method.getName();
			if (MapDocument.class.isAssignableFrom(declaringClass)) {
				if (name.equals("append")) {
					String key = null;
					if (args.length > 0 && args[0] != null && args[0] instanceof PropGen) {
						key = InnerMappers.propKey(InnerMappers.propGen(((PropGen) args[0]), target.getBeanClass()));
					} else {
						key = args.length > 0 && args[0] != null ? String.valueOf(args[0]) : null;
					}
					Object value = args.length > 1 ? args[1] : null;
					target.getDocument().append(key, InnerCodecs.encode(value, null, null, null));
					return obj; // fixme

				} else if (name.equals("exec")) {
					if (args.length > 0 && args[0] != null && args[0] instanceof PropGen.Exec) {
						((PropGen.Exec) args[0]).accept();
					}
					return obj; // fixme
				}

				return method.invoke(target, args);
			}
			if (name.equals("append")) {
				String key = args.length > 0 && args[0] != null ? String.valueOf(args[0]) : null;
				Object val = null;
				if (args.length > 1) {
					val = InnerCodecs.encode(args[1], null, null, null);
				}
				target.getDocument().append(key, val);
				return obj; // fixme

			} else if (name.equals("put")) {
				String key = args.length > 0 && args[0] != null ? String.valueOf(args[0]) : null;
				Object val = null;
				if (args.length > 1) {
					val = InnerCodecs.encode(args[1], null, null, null);
				}
				return target.getDocument().put(key, val);

			} else if (name.startsWith("get")) {
				if (args.length > 0 && args[0] instanceof PropGen) {
					args[0] = InnerMappers.propKey(InnerMappers.propGen(((PropGen) args[0]), target.getBeanClass()));
				}
				return method.invoke(target.getDocument(), args);
			}
			return method.invoke(target.getDocument(), args);
		}
	}
}
