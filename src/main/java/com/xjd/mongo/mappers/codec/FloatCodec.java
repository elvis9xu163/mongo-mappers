package com.xjd.mongo.mappers.codec;


import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class FloatCodec implements Codec<Float> {

	@Override
	public Object encode(Float source, Context context) {
		return source;
	}

	@Override
	public Float decode(Object source, Context context) {
		if (source == null) {
			return null;
		}

		if (source instanceof String) {
			return Float.valueOf((String) source);

		} else if (source instanceof Number) {
			return ((Number) source).floatValue();

		} else if (source instanceof Boolean) {
			return ((Boolean) source) ? 1F : 0;
		}

		return (Float) source;
	}
}
