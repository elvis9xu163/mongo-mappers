package com.xjd.mongo.mappers.codec;

import org.bson.Document;

import com.xjd.mongo.mappers.Codec;
import com.xjd.mongo.mappers.core.InnerCodecs;
import com.xjd.mongo.mappers.core.InnerResovers;
import com.xjd.mongo.mappers.core.assist.Reflections;
import com.xjd.mongo.mappers.core.bean.EntityMeta;
import com.xjd.mongo.mappers.core.bean.PropertyMeta;


/**
 * @author elvis.xu
 * @since 2018-09-07 02:01
 */
public class ObjectCodec implements Codec<Object> {
	@Override
	public Object encode(Object source, Context context) {
		if (source == null) return null;

		Document document = new Document();
		Class<?> type = source.getClass();

		if (type.equals(Object.class)) return document; // Object就用不浪费时间了

		EntityMeta entityMeta = InnerResovers.getEntityMeta(type);
		for (PropertyMeta propertyMeta : entityMeta.getPropertyMetaMap().values()) {
			if (propertyMeta.getGetterMeta() == null || propertyMeta.resolveIgnored(true)) continue; // getter需要忽略
			if (!context.includePath(context.concatPropertyPath(propertyMeta.getName()))) continue; //指定不包含的属性也跳过
			Object propertyValue = Reflections.invoke(propertyMeta.getGetterMeta().getMethod(), source, null);
			if (propertyValue == null && !context.withNull()) continue; // 不允许含null

			context.pushPropertyPath(propertyMeta.getName());
			document.append(propertyMeta.resolveKey(true),
					InnerCodecs.encode(propertyValue, propertyMeta.getGetterMeta().getType(), propertyMeta.resolveCodecClass(true), context));
			context.popPropertyPath();
		}

		return document;
	}

	@Override
	public Object decode(Object source, Context context) {
		if (source == null) return null;

		// FIXME

		return null;
	}
}
