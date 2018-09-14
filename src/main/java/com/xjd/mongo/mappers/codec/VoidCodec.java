package com.xjd.mongo.mappers.codec;

import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-08 22:09
 */
public class VoidCodec implements Codec<Void> {
	@Override
	public Object encode(Void source, Context context) {
		return source;
	}

	@Override
	public Void decode(Object source, Context context) {
		return null;
	}
}
