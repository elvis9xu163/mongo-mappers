package com.xjd.mongo.mappers.codec;

import org.bson.conversions.Bson;

import com.xjd.mongo.mappers.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-09 13:11
 */
public class BsonCodec implements Codec<Bson> {
	@Override
	public Object encode(Bson source, Context context) {
		return source;
	}

	@Override
	public Bson decode(Object source, Context context) {
		return (Bson) source;
	}
}
