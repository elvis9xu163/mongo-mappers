package com.xjd.mongo.mappers5;

/**
 * @author elvis.xu
 * @since 2018-09-06 22:25
 */
public interface Codec<T> {
	Object encode(T bean);
	T decode(Object doc);

	static Codec NULL = new Codec() {
		@Override
		public Object encode(Object o) {
			return null;
		}

		@Override
		public Object decode(Object obj) {
			return null;
		}
	};
}
