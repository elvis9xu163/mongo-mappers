package com.xjd.mongo.mappers.codec;


import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class ByteCodec implements Codec<Byte> {

	@Override
	public Object encode(Byte source, Context context) {
		return source;
	}

	@Override
	public Byte decode(Object source, Context context) {
		if (source == null) {
			return null;
		}

		if (source instanceof String) {
			return Byte.valueOf((String) source);

		} else if (source instanceof Number) {
			return ((Number) source).byteValue();

		} else if (source instanceof Boolean) {
			return ((Boolean) source) ? (byte) 1 : 0;
		}

		return (Byte) source;
	}
}
