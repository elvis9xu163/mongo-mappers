package com.xjd.mongo.mappers5.codec;

import com.xjd.mongo.mappers5.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class ShortCodec implements Codec<Short> {
	@Override
	public Object encode(Short bean) {
		return bean;
	}

	@Override
	public Short decode(Object doc) {
		if (doc == null) {
			return null;
		}

		if (doc instanceof String) {
			return Short.valueOf((String) doc);

		} else if (doc instanceof Number) {
			return ((Number) doc).shortValue();

		} else if (doc instanceof Boolean) {
			return ((Boolean) doc) ? (short) 1 : 0;
		}

		return (Short) doc;
	}
}
