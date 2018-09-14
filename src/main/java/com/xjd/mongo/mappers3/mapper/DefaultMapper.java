package com.xjd.mongo.mappers3.mapper;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.xjd.mongo.mappers3.Mapper;

/**
 * @author elvis.xu
 * @since 2018-09-05 18:19
 */
@Slf4j
public class DefaultMapper<T> implements Mapper<T> {
	ThreadLocal<DefaultProperty> threadLocal = new ThreadLocal<>();
	protected Class<T> beanClass;
	protected T proxy;

	public DefaultMapper(Class<T> beanClass) {
		this.beanClass = beanClass;
		proxy = MapperProxy.proxy(beanClass, threadLocal);
	}


	@Override
	public String key(GetterInvoker<T> getterInvoker) {
		Property property = property(getterInvoker);
		return property == null ? null : property.key();
	}

	@Override
	public String name(GetterInvoker<T> getterInvoker) {
		Property property = property(getterInvoker);
		return property == null ? null : property.name();
	}

	@Override
	public Property property(GetterInvoker<T> getterInvoker) {
		threadLocal.remove();
		getterInvoker.invokeGetter(proxy);
		DefaultProperty property = threadLocal.get();
		threadLocal.remove();
		return property;
	}

	protected static class DefaultProperty implements Property {
		protected List<PropertyMapper> propChain = new ArrayList<>();

		protected void addProp(PropertyMapper prop) {
			propChain.add(prop);
		}

		@Override
		public String key() {
			return toPropString(true);
		}

		@Override
		public String name() {
			return toPropString(false);
		}

		protected String toPropString(boolean key) {
			return propChain.isEmpty() ? "" : (key ? propChain.get(propChain.size() - 1).getKey() : propChain.get(propChain.size() - 1).getName());
		}

		protected String toPropHierarchyString(boolean key) {
			StringBuilder buf = new StringBuilder();
			for (PropertyMapper prop : propChain) {
				buf.append(".").append(key ? prop.getKey() : prop.getName());
			}
			if (buf.length() > 0) {
				buf.deleteCharAt(0);
			}
			return buf.toString();
		}

		@Override
		public String[] keyChain() {
			return toPropChain(true);
		}

		@Override
		public String[] nameChain() {
			return toPropChain(false);
		}

		protected String[] toPropChain(boolean key) {
			String[] chain = new String[propChain.size()];
			int i = 0;
			for (PropertyMapper prop : propChain) {
				chain[i++] = key ? prop.getKey() : prop.getName();
			}
			return chain;
		}
	}


}
