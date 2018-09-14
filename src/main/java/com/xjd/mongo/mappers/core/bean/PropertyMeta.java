package com.xjd.mongo.mappers.core.bean;

import java.lang.reflect.Method;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-08 12:49
 */
@Getter
@Setter
@Accessors(chain = true)
public class PropertyMeta extends PropertyAnnoMeta {
	protected String name;
	protected MethodMeta getterMeta;
	protected MethodMeta setterMeta;

	public String resolveKey(boolean isGetter) {
		MethodMeta methodMeta = isGetter ? getterMeta : setterMeta;
		if (methodMeta == null) return null;
		String key = methodMeta.key != null ? methodMeta.key : this.key;
		return key == null || key.equals("") ? this.name : key;
	}

	public Boolean resolveIgnored(boolean isGetter) {
		MethodMeta methodMeta = isGetter ? getterMeta : setterMeta;
		if (methodMeta == null) return null;
		return methodMeta.ignored != null ? methodMeta.ignored : (this.ignored != null ? this.ignored : false);
	}

	public Class<? extends Codec> resolveCodecClass(boolean isGetter) {
		MethodMeta methodMeta = isGetter ? getterMeta : setterMeta;
		if (methodMeta == null) return null;
		return methodMeta.codecClass != null ? methodMeta.codecClass : (this.codecClass != null ? this.codecClass : null);
	}

	public Class<?> resolveMockClass(boolean isGetter) {
		MethodMeta methodMeta = isGetter ? getterMeta : setterMeta;
		if (methodMeta == null) return null;
		return methodMeta.mockClass != null ? methodMeta.mockClass : (this.mockClass != null ? this.mockClass : null);
	}

	public Class<?> resolveComponentClass(boolean isGetter) {
		MethodMeta methodMeta = isGetter ? getterMeta : setterMeta;
		if (methodMeta == null) return null;
		return methodMeta.componentClass != null ? methodMeta.componentClass : (this.componentClass != null ? this.componentClass : null);
	}

	@Getter
	@Setter
	@Accessors(chain = true)
	public static class MethodMeta extends PropertyAnnoMeta {
		protected Class type;
		protected Method method;
	}
}
