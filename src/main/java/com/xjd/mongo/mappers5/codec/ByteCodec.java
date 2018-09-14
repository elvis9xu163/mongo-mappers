package com.xjd.mongo.mappers5.codec;

import com.xjd.mongo.mappers5.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class ByteCodec implements Codec<Byte> {
	@Override
	public Object encode(Byte bean) {
		return bean;
	}

	@Override
	public Byte decode(Object doc) {
		if (doc == null) {
			return null;
		}

		if (doc instanceof String) {
			return Byte.valueOf((String) doc);

		} else if (doc instanceof Number) {
			return ((Number) doc).byteValue();

		} else if (doc instanceof Boolean) {
			return ((Boolean) doc) ? (byte) 1 : 0;
		}

		return (Byte) doc;
	}
}
