package com.xjd.mongo.mappers2;

import com.xjd.mongo.mappers3.Mappers;

/**
 * @author elvis.xu
 * @since 2018-09-05 23:00
 */
public class Test {
	public static void main(String[] args) {
		{
			String key = Mappers.of(TestBean.class).key(TestBean::getId);
			System.out.println(key);
		}
		{
			String key = Mappers.of(TestBean.class).key(p -> {p.getName();p.getId();});
			System.out.println(key);
		}
		{
			String key = Mappers.of(TestBean.class).key(p -> {p.getAddress();});
			System.out.println(key);
		}
		{
			String key = Mappers.of(TestBean.class).key(p -> {p.getAddress().getFormat();});
			System.out.println(key);
		}
		{
			String key = Mappers.of(TestBean.class).key(p -> {p.getAddressList();});
			System.out.println(key);
		}
//		{
//			String key = Mappers.of(TestBean.class).key(p -> {p.getAddressList().get(0);});
//			System.out.println(key);
//		}
		{
			String key = Mappers.of(TestBean.class).key(p -> p.getAge());
			System.out.println(key);
		}
		{
			String key = Mappers.of(TestBean.class).key(p -> {p.getAge2();});
			System.out.println(key);
		}
	}
}
