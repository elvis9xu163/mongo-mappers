package com.xjd.mongo.mappers.codec;


import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class LongCodec implements Codec<Long> {

	@Override
	public Object encode(Long source, Context context) {
		return source;
	}

	@Override
	public Long decode(Object source, Context context) {
		if (source == null) {
			return null;
		}

		if (source instanceof String) {
			return Long.valueOf((String) source);

		} else if (source instanceof Number) {
			return ((Number) source).longValue();

		} else if (source instanceof Boolean) {
			return ((Boolean) source) ? 1L : 0;
		}

		return (Long) source;
	}
}
