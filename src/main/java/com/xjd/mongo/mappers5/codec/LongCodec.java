package com.xjd.mongo.mappers5.codec;

import com.xjd.mongo.mappers5.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class LongCodec implements Codec<Long> {
	@Override
	public Object encode(Long bean) {
		return bean;
	}

	@Override
	public Long decode(Object doc) {
		if (doc == null) {
			return null;
		}

		if (doc instanceof String) {
			return Long.valueOf((String) doc);

		} else if (doc instanceof Number) {
			return ((Number) doc).longValue();

		} else if (doc instanceof Boolean) {
			return ((Boolean) doc) ? 1L : 0;
		}

		return (Long) doc;
	}
}
