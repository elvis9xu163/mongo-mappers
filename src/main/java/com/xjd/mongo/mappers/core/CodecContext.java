package com.xjd.mongo.mappers.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xjd.mongo.mappers.Codec;
import com.xjd.mongo.mappers.core.bean.ThreadLocalData;

/**
 * @author elvis.xu
 * @since 2018-09-08 14:59
 */
public class CodecContext implements Codec.Context {
	protected List<String> currentPath = new LinkedList<>();
	protected boolean withNull;
	protected List<List<ThreadLocalData.PropertyNode>> includes;
	protected List<List<ThreadLocalData.PropertyNode>> excludes;
	protected Map<Object, Object> cache = new HashMap<>();

	public CodecContext(boolean withNull, List<List<ThreadLocalData.PropertyNode>> includes, List<List<ThreadLocalData.PropertyNode>> excludes) {
		this.withNull = withNull;
		this.includes = includes;
		this.excludes = excludes;
	}

	@Override
	public boolean withNull() {
		return withNull;
	}

	@Override
	public String currentPropertyPath() {
		StringBuilder buf = new StringBuilder();
		for (String node : currentPath) {
			if (node.charAt(0) != '[') buf.append(".");
			buf.append(node);
		}
		return buf.length() > 0 && buf.charAt(0) != '[' ? buf.deleteCharAt(0).toString() : buf.toString();
	}

	@Override
	public void pushPropertyPath(String propertyName) {
		currentPath.add(propertyName);
	}

	@Override
	public void pushPropertyPath(Integer listIndex, Object mapKey) {
		if (listIndex != null) {
			currentPath.add("[" + listIndex + "]");
		} else {
			if (mapKey == null) {
				currentPath.add("[null]");
			} else {
				currentPath.add("['" + mapKey + "']");
			}
		}
	}

	@Override
	public void popPropertyPath() {
		int size = currentPath.size();
		if (size > 0) currentPath.remove(size - 1);
	}

	@Override
	public String concatPropertyPath(String propertyName) {
		String path = currentPropertyPath();
		return path.length() > 0 ? path + "." + propertyName : propertyName;
	}

	@Override
	public boolean includePath(String path) {
		if (includes != null) {
			return doIncludePaths(path, includes, true);

		} else if  (excludes != null) {
			return !doIncludePaths(path, excludes, false);
		}
		return true;
	}

	protected boolean doIncludePaths(String path, List<List<ThreadLocalData.PropertyNode>> props, boolean include) {
		String[] nodes = path.replaceAll("\\[[^\\[\\]]*\\]", "").split("\\."); // 去除数组和Map
		for (List<ThreadLocalData.PropertyNode> prop : props) {
			if (doIncludePaths(nodes, prop, include)) return true;
		}
		return false;
	}

	protected boolean doIncludePaths(String[] nodes, List<ThreadLocalData.PropertyNode> props, boolean include) {
		if (props.size() == 0) return false;
		if (nodes.length == 0) return include;
		int j = 0;
		for (int i = 0, size = props.size(); i < size; i++) {
			ThreadLocalData.PropertyNode propertyNode = props.get(i);
			if (propertyNode.getPropertyMeta() == null) continue; // 说明是[],算成功

			if (j == nodes.length) { // nodes已完全匹配, 但是props还没完, nodes长度小于props
				return include;
			}

			String base = nodes[j++];
//			while (base.charAt(0) == '[' && j < nodes.length) { // 说明是[],算成功
//				base = nodes[j++];
//			}
//
//			if (base.charAt(0) == '[') { // 说明是[],算成功, nodes已遍历完, 但是props还没完, nodes长度小于props
//				return  include;
//			}

			if (!base.equals(propertyNode.getPropertyMeta().getName())) { // 如果路径不匹配直接false
				return false;
			}

		}
		return true; // props遍历完, 说明nodes长度>=props
	}

	@Override
	public void put(Object key, Object value) {
		cache.put(key, value);
	}

	@Override
	public Object get(Object key) {
		return cache.get(key);
	}
}
