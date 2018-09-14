package com.xjd.mongo.mappers.codec;

import java.util.HashMap;
import java.util.Map;

import com.xjd.mongo.mappers.Codec;
import com.xjd.mongo.mappers.core.InnerCodecs;

/**
 * @author elvis.xu
 * @since 2018-09-08 22:09
 */
public class MapCodec implements Codec<Map<?, ?>> {
	@Override
	public Object encode(Map<?, ?> source, Context context) {
		if (source == null) return source;

		Map<String, Object> map = new HashMap<>(source.size());
		for (Map.Entry<?, ?> entry : source.entrySet()) {
			String key = String.valueOf(entry.getKey());
			Object value = entry.getValue();
			context.pushPropertyPath(null, key);
			map.put(key, InnerCodecs.encode(value, value == null ? null : value.getClass(), null, context));
			context.popPropertyPath();
		}
		return map;
	}

	@Override
	public Map decode(Object source, Context context) {
		return null;
	}
}
