package com.xjd.mongo.mappers.core;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.xjd.mongo.mappers.Codec;
import com.xjd.mongo.mappers.PropGen;
import com.xjd.mongo.mappers.core.bean.EntityMeta;
import com.xjd.mongo.mappers.core.bean.ThreadLocalData;
import com.xjd.mongo.mappers5.core.Reflections;

/**
 * @author elvis.xu
 * @since 2018-09-08 11:41
 */
public abstract class InnerMappers {
	public static final ThreadLocal<ThreadLocalData> threadLocalData = new ThreadLocal<>();

	// ==== ThreadLocal ==== //
	public static ThreadLocalData getThreadData() {
		return threadLocalData.get();
	}

	public static ThreadLocalData getAndCreateThreadData() {
		ThreadLocalData data = getThreadData();
		if (data == null) {
			data = new ThreadLocalData();
			threadLocalData.set(data);
		}
		return data;
	}

	public static void indent(int indent) {
		ThreadLocalData data = getAndCreateThreadData();
		data.setIndent(indent);
	}

	public static void indentReset() {
		ThreadLocalData data = getThreadData();
		if (data != null) {
			data.setIndent(0);
		}
	}

	public static void propReset() {
		ThreadLocalData data = getThreadData();
		if (data != null) {
			data.setPropPath(new ArrayList<>());
		}
	}

	public static void propAdd(ThreadLocalData.PropertyNode prop) {
		getAndCreateThreadData().getPropPath().add(prop);
	}

	public static List<ThreadLocalData.PropertyNode> prop() {
		ThreadLocalData data = getThreadData();
		if (data != null) {
			List<ThreadLocalData.PropertyNode> path = data.getPropPath();
			if (data.getIndent() == 0) return path;
			if (data.getIndent() >= path.size()) {
				return new ArrayList<>(0);
			}
			if (data.getIndent() < 0 && Math.abs(data.getIndent()) >= path.size()) {
				return path;
			}
			List<ThreadLocalData.PropertyNode> indentedPath = new ArrayList<>(Math.abs(data.getIndent()));
			for (int i = data.getIndent() > 0 ? data.getIndent() : (path.size() + data.getIndent()), size = path.size(); i < size; i++) {
				indentedPath.add(path.get(i));
			}
			return indentedPath;
		}
		return new ArrayList<>(0);
	}

	public static void propExec(PropGen exec, Class clazz) {
		exec.accept(InnerMockers.getMock(clazz, clazz, null));
	}

	public static List<ThreadLocalData.PropertyNode>  propGen(PropGen exec, Class clazz) {
		propReset();
		propExec(exec, clazz);
		List<ThreadLocalData.PropertyNode> rt = prop();
		propReset();
		return rt;
	}

	public static <T> List<List<ThreadLocalData.PropertyNode>>  propGen(PropGen.Multi<T> multi, T mock) {
		List list = new ArrayList();
		propReset();
		if (multi == null) return list;
		multi.accept(mock, d -> {
			list.add(prop());
			propReset();
		});
		propReset();
		return list;
	}

	public static String propKey(List<ThreadLocalData.PropertyNode> props) {
		if (props == null) return null;
		if (props.isEmpty()) return "";
		StringBuilder buf = new StringBuilder();
		for (ThreadLocalData.PropertyNode prop : props) {
			if (prop.getPropertyMeta() != null) {
				buf.append(".").append(prop.getPropertyMeta().resolveKey(true));
			} else {
				buf.append(prop.getListIndex() != null ? "[" + prop.getListIndex() + "]" : "['" + prop.getMapKey() + "']");
			}
		}
		return buf.length() > 0 && buf.charAt(0) == '.' ? buf.deleteCharAt(0).toString() : buf.toString();
	}


	// === encode === //
	public static <T> Document encode(T t, Class<T> clazz, boolean withNull, PropGen.Multi<T> includeProps, PropGen.Multi<T> excludeProps) {
		Class<T> type = clazz != null ? clazz : (t != null ? (Class<T>) t.getClass() : null);
		EntityMeta entityMeta = type == null ? null : InnerResovers.getEntityMeta(type);
		Class<? extends Codec> codecClass = entityMeta == null ? null : entityMeta.getCodecClass();
		if (t == null) {
			return (Document) InnerCodecs.encode(t, clazz, codecClass, null);
		}

		// 计算includeProps等
		Class<?> mockClass = entityMeta == null ? null : entityMeta.getMockClass();
		mockClass = mockClass != null && !Reflections.isVoid(mockClass) ? mockClass : type;
		List<List<ThreadLocalData.PropertyNode>> includes = includeProps == null ? null : propGen(includeProps, (T) InnerMockers.getMock((Class<? extends T>) mockClass, type, null));
		List<List<ThreadLocalData.PropertyNode>> excludes = includeProps != null || excludeProps == null ? null : propGen(excludeProps, (T) InnerMockers.getMock((Class<? extends T>) mockClass, type, null));
		CodecContext codecContext = InnerCodecs.createCodecContext(withNull, includes, excludes);

		return (Document) InnerCodecs.encode(t, clazz, codecClass, codecContext);
	}

}
