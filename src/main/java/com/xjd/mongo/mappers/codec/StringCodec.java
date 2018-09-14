package com.xjd.mongo.mappers.codec;

import com.xjd.mongo.mappers.Codec;


/**
 * @author elvis.xu
 * @since 2018-09-07 04:01
 */
public class StringCodec implements Codec<String> {

	@Override
	public Object encode(String source, Context context) {
		return source;
	}

	@Override
	public String decode(Object source, Context context) {
		if (source == null) return null;

		if (source instanceof String) {
			return (String) source;
		}

		return source.toString();
	}
}
