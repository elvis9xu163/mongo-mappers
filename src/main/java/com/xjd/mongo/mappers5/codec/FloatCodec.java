package com.xjd.mongo.mappers5.codec;

import com.xjd.mongo.mappers5.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class FloatCodec implements Codec<Float> {
	@Override
	public Object encode(Float bean) {
		return bean;
	}

	@Override
	public Float decode(Object doc) {
		if (doc == null) {
			return null;
		}

		if (doc instanceof String) {
			return Float.valueOf((String) doc);

		} else if (doc instanceof Number) {
			return ((Number) doc).floatValue();

		} else if (doc instanceof Boolean) {
			return ((Boolean) doc) ? 1F : 0;
		}

		return (Float) doc;
	}
}
