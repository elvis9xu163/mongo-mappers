package com.xjd.mongo.mappers.codec;

import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-08 23:00
 */
public class BooleanCodec implements Codec<Boolean> {
	@Override
	public Object encode(Boolean source, Context context) {
		return source;
	}

	@Override
	public Boolean decode(Object source, Context context) {
		if (source == null) {
			return null;
		}

		if (source instanceof String) {
			return Boolean.valueOf((String) source);

		} else if (source instanceof Number) {
			return ((Number) source).intValue() != 0;

		}

		return (Boolean) source;
	}
}
