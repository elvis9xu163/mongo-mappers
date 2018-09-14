package com.xjd.mongo.mappers5.codec;

import org.bson.Document;

import com.xjd.mongo.mappers5.Codec;

/**
 * @author elvis.xu
 * @since 2018-09-07 04:01
 */
public class StringCodec implements Codec<String> {
	@Override
	public Object encode(String bean) {
		return bean;
	}

	@Override
	public String decode(Object doc) {
		if (doc == null) return null;

		if (doc instanceof String) {
			return (String) doc;

		} else if (doc instanceof Document) {
			return ((Document) doc).toJson();

		}

		return doc.toString();
	}
}
