package com.xjd.mongo.mappers5.codec;

import java.lang.reflect.Method;
import java.util.Map;

import org.bson.Document;

import com.xjd.mongo.mappers5.Codec;
import com.xjd.mongo.mappers5.core.Codecs;
import com.xjd.mongo.mappers5.core.MapperImpl;
import com.xjd.mongo.mappers5.core.Mappers;
import com.xjd.mongo.mappers5.core.inner.PropertyMeta;

/**
 * @author elvis.xu
 * @since 2018-09-07 02:01
 */
public class BeanCodec implements Codec<Object> {
	@Override
	public Object encode(Object o) {
		if (o == null) return null;

		Document document = new Document();
		MapperImpl<?> mapper = Mappers.of(o.getClass());
		for (Map.Entry<Method, PropertyMeta> entry : mapper.getGetterMap().entrySet()) {
			PropertyMeta meta = entry.getValue();
			try {
				Object encoded = Codecs.encode(meta.getGetter().getMethod().invoke(o), meta.getGetter().getType(), meta.getterCodec());
				document.append(meta.getterKey(), encoded);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}

		return document;
	}

	@Override
	public Object decode(Object obj) {
		if (obj == null) return null;
		return null;
	}
}
