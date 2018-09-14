package com.xjd.mongo.mappers.codec;

import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class DoubleCodec implements Codec<Double> {

	@Override
	public Object encode(Double source, Context context) {
		return source;
	}

	@Override
	public Double decode(Object source, Context context) {
		if (source == null) {
			return null;
		}

		if (source instanceof String) {
			return Double.valueOf((String) source);

		} else if (source instanceof Number) {
			return ((Number) source).doubleValue();

		} else if (source instanceof Boolean) {
			return ((Boolean) source) ? 1D : 0;
		}

		return (Double) source;
	}
}
