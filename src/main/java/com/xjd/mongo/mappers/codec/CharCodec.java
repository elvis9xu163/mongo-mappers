package com.xjd.mongo.mappers.codec;


import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:09
 */
public class CharCodec implements Codec<Character> {

	@Override
	public Object encode(Character source, Context context) {
		return source;
	}

	@Override
	public Character decode(Object source, Context context) {
		if (source == null) {
			return null;
		}

		if (source instanceof String || source instanceof Number) {
			String s = source.toString();
			if (s.length() == 1) return s.charAt(0);
			if (s.length() == 0) return null;

		} else if (source instanceof Boolean) {
			return ((Boolean) source) ? '1' : '0';
		}

		return (Character) source;
	}
}
