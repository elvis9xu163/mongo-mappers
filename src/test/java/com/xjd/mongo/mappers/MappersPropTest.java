package com.xjd.mongo.mappers;

import com.xjd.mongo.mappers5.TestBean;

/**
 * @author elvis.xu
 * @since 2018-09-08 21:45
 */
public class MappersPropTest {
	public static void main(String[] args) {
		Mapper<TestBean> mapper = Mappers.of(TestBean.class);
		System.out.println(mapper.entity());
		Mappers.indent(1);
		System.out.println(mapper.prop(TestBean::getName));
		System.out.println(mapper.prop(t -> t.getAddress().getCode()));
		System.out.println(mapper.prop(t -> t.getAds().get(1).getCode()));
	}
}
