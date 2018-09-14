package com.xjd.mongo.mappers5.core.inner;

import java.lang.reflect.Method;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.xjd.mongo.mappers5.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-06 22:23
 */
@Getter
@Setter
@Accessors(chain = true)
public class PropertyMeta {
	protected String name;
	protected String key;
	protected Codec codec;
	protected PropertyMethod getter;
	protected PropertyMethod setter;

	@Getter
	@Setter
	@Accessors(chain = true)
	public static class PropertyMethod {
		protected String key;
		protected Class type;
		protected Object mock;
		protected Codec codec;
		protected Method method;

	}

	public Codec getterCodec() {
		Codec codec = getter != null ? getter.codec : null;
		if (codec == null) {
			codec = this.codec;
		}
		return codec;
	}

	public Codec setterCodec() {
		Codec codec = setter != null ? setter.codec : null;
		if (codec == null) {
			codec = this.codec;
		}
		return codec;
	}

	public String getterKey() {
		String key = getter != null ? getter.getKey() : null;
		return key != null ? key : (this.key != null ? this.key : this.name);
	}

	public String setterKey() {
		String key = setter != null ? setter.getKey() : null;
		return key != null ? key : (this.key != null ? this.key : this.name);
	}
}
