package com.xjd.mongo.mappers5.codec;

import com.xjd.mongo.mappers5.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class IntCodec implements Codec<Integer> {
	@Override
	public Object encode(Integer bean) {
		return bean;
	}

	@Override
	public Integer decode(Object doc) {
		if (doc == null) {
			return null;
		}

		if (doc instanceof String) {
			return Integer.valueOf((String) doc);

		} else if (doc instanceof Number) {
			return ((Number) doc).intValue();

		} else if (doc instanceof Boolean) {
			return ((Boolean) doc) ? 1 : 0;
		}

		return (Integer) doc;
	}
}
