package com.xjd.mongo.mappers3.util;

/**
 * @author elvis.xu
 * @since 2018-09-05 21:44
 */
public class StringUtil {
	public static String trimToNull(String str) {
		if (str == null) return str;
		str = str.trim();
		return str.equals("") ? null : str;
	}
}
