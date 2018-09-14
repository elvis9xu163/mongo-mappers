package com.xjd.mongo.mappers4;

import java.util.Date;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * @author elvis.xu
 * @since 2018-09-06 18:17
 */
public class MappedDocument<E> extends Document {
	protected Document delegatee;
	protected Mapper<E> mapper;

	public MappedDocument(Mapper<E> mapper) {
		this(mapper, new Document());
	}

	protected MappedDocument(Mapper<E> mapper, Document delegatee) {
		this.mapper = mapper;
		this.delegatee = delegatee;
	}

	protected String propKey(Consumer<E> getter) {
		return mapper.prop(getter);
	}
	
	public MappedDocument<E> exec(Runnable runnable) {
		runnable.run();
		return this;
	}

	public MappedDocument<E> append(Consumer<E> getter, Object value) {
		String key = propKey(getter);
		if (delegatee != null) {
			delegatee.append(key, value);
		} else {
			super.append(key, value);
		}
		return this;
	}

	public <T> T get(Consumer<E> getter, Class<T> clazz) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.get(key, clazz);
		} else {
			return super.get("", clazz);
		}
	}

	public <T> T get(Consumer<E> getter, T defaultValue) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.get(key, defaultValue);
		} else {
			return super.get(key, defaultValue);
		}
	}

	public Integer getInteger(Consumer<E> getter) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.getInteger(key);
		} else {
			return super.getInteger(key);
		}
	}

	public int getInteger(Consumer<E> getter, int defaultValue) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.getInteger(key, defaultValue);
		} else {
			return super.getInteger(key, defaultValue);
		}
	}

	public Long getLong(Consumer<E> getter) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.getLong(key);
		} else {
			return super.getLong(key);
		}
	}

	public Double getDouble(Consumer<E> getter) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.getDouble(key);
		} else {
			return super.getDouble(key);
		}
	}

	public String getString(Consumer<E> getter) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.getString(key);
		} else {
			return super.getString(key);
		}
	}

	public Boolean getBoolean(Consumer<E> getter) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.getBoolean(key);
		} else {
			return super.getBoolean(key);
		}
	}

	public boolean getBoolean(Consumer<E> getter, boolean defaultValue) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.getBoolean(key, defaultValue);
		} else {
			return super.getBoolean(key, defaultValue);
		}
	}

	public ObjectId getObjectId(Consumer<E> getter) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.getObjectId(key);
		} else {
			return super.getObjectId(key);
		}
	}

	public Date getDate(Consumer<E> getter) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.getDate(key);
		} else {
			return super.getDate(key);
		}
	}

	public Object get(Consumer<E> getter) {
		String key = propKey(getter);
		if (delegatee != null) {
			return delegatee.get(key);
		} else {
			return super.get(key);
		}
	}

	public static MappedDocument delegate(Document document) {
		// fixme
		return null;
	}

}
