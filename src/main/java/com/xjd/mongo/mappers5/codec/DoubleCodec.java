package com.xjd.mongo.mappers5.codec;

import com.xjd.mongo.mappers5.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class DoubleCodec implements Codec<Double> {
	@Override
	public Object encode(Double bean) {
		return bean;
	}

	@Override
	public Double decode(Object doc) {
		if (doc == null) {
			return null;
		}

		if (doc instanceof String) {
			return Double.valueOf((String) doc);

		} else if (doc instanceof Number) {
			return ((Number) doc).doubleValue();

		} else if (doc instanceof Boolean) {
			return ((Boolean) doc) ? 1D : 0;
		}

		return (Double) doc;
	}
}
