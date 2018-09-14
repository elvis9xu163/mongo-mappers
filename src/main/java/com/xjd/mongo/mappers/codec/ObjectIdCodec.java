package com.xjd.mongo.mappers.codec;

import org.bson.types.ObjectId;

import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-10 16:24
 */
public class ObjectIdCodec implements Codec<ObjectId> {
	@Override
	public Object encode(ObjectId source, Context context) {
		return source;
	}

	@Override
	public ObjectId decode(Object source, Context context) {
		if (source == null) return null;

		if (source instanceof String) {
			return new ObjectId((String) source);
		}

		return (ObjectId) source;
	}
}
