package com.xjd.mongo.mappers.codec;


import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class ShortCodec implements Codec<Short> {

	@Override
	public Object encode(Short source, Context context) {
		return source;
	}

	@Override
	public Short decode(Object source, Context context) {
		if (source == null) {
			return null;
		}

		if (source instanceof String) {
			return Short.valueOf((String) source);

		} else if (source instanceof Number) {
			return ((Number) source).shortValue();

		} else if (source instanceof Boolean) {
			return ((Boolean) source) ? (short) 1 : 0;
		}

		return (Short) source;
	}
}
