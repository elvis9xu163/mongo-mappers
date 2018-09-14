package com.xjd.mongo.mappers.util;

/**
 * @author elvis.xu
 * @since 2018-09-08 13:28
 */
public abstract class StringUtil {
	public static String trim(String str) {
		return str != null ? str.trim() : str;
	}
}
