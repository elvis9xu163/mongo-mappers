package com.xjd.mongo.mappers.codec;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import com.xjd.mongo.mappers.Codec;
import com.xjd.mongo.mappers.core.InnerCodecs;

/**
 * @author elvis.xu
 * @since 2018-09-08 22:45
 */
public class PrimitiveArrayCodec implements Codec<Object> {
	@Override
	public Object encode(Object source, Context context) {
		if (source == null) return source;


		int count = Array.getLength(source);
		List list = new LinkedList();
		for (int i = 0; i < count; i++) {
			context.pushPropertyPath(i, null);
			Object item = Array.get(source, i);
			list.add(InnerCodecs.encode(item, item == null ? null : item.getClass(), null, context));
			context.popPropertyPath();
		}

		return list;
	}

	@Override
	public Object[] decode(Object source, Context context) {
		return new Object[0];
	}
}
