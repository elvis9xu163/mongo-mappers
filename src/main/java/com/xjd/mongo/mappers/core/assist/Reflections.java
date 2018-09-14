package com.xjd.mongo.mappers.core.assist;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author elvis.xu
 * @since 2018-09-08 13:43
 */
public abstract class Reflections {
	public static boolean isGetter(Method method) {
		if (method.getDeclaringClass().equals(Object.class)) return false; // Object中的getter不算
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

	public static boolean isSetter(Method method) {
		if (method.getDeclaringClass().equals(Object.class)) return false; // Object中的setter不算
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

	public static boolean isVoid(Class clazz) {
		return void.class.equals(clazz) || Void.class.equals(clazz);
	}

	public static boolean isBoolean(Class clazz) {
		return boolean.class.equals(clazz) || Boolean.class.equals(clazz);
	}

	public static String resolvePropertyName(Method method) {
		String name = method.getName();
		int index = name.startsWith("get") || name.startsWith("set") ? 3 : (name.startsWith("is") ? 2 : -1);
		if (index < 0) return null;
		return Character.toLowerCase(name.charAt(index)) + (name.length() > (index + 1) ? name.substring(index + 1) : "");
	}

	public static Object invoke(Method method, Object source, Object... arguments) {
		if (method == null) throw new IllegalArgumentException("method cannot be null");
		try {
			return method.invoke(source, arguments);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static boolean isListGet(Method method) {
		return List.class.isAssignableFrom(method.getDeclaringClass()) && method.getName().equals("get")
				&& method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(int.class);
	}

	public static boolean isMapGet(Method method) {
		return Map.class.isAssignableFrom(method.getDeclaringClass()) && method.getName().equals("get")
				&& method.getParameterCount() == 1;
	}
}
