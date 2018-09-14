package com.xjd.mongo.mappers.codec;


import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class IntCodec implements Codec<Integer> {
	@Override
	public Object encode(Integer source, Context context) {
		return source;
	}

	@Override
	public Integer decode(Object source, Context context) {
		if (source == null) {
			return null;
		}

		if (source instanceof String) {
			return Integer.valueOf((String) source);

		} else if (source instanceof Number) {
			return ((Number) source).intValue();

		} else if (source instanceof Boolean) {
			return ((Boolean) source) ? 1 : 0;
		}

		return (Integer) source;
	}
}
