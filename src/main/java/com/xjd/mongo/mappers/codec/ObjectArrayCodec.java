package com.xjd.mongo.mappers.codec;

import java.util.LinkedList;
import java.util.List;

import com.xjd.mongo.mappers.Codec;
import com.xjd.mongo.mappers.core.InnerCodecs;

/**
 * @author elvis.xu
 * @since 2018-09-08 22:45
 */
public class ObjectArrayCodec implements Codec<Object[]> {
	@Override
	public Object encode(Object[] source, Context context) {
		if (source == null) return source;

		int i = 0;
		List list = new LinkedList();
		for (Object item : source) {
			context.pushPropertyPath(i, null);
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
