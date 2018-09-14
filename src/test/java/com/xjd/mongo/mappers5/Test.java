package com.xjd.mongo.mappers5;


/**
 * @author elvis.xu
 * @since 2018-09-06 22:12
 */
public class Test {
	public static void main(String[] args) {
		Mapper<TestBean> mapp = Mapper.of(TestBean.class);
		TestBean bean = new TestBean();
		bean.setName("xxx");
		bean.setAge(10);
		System.out.println(mapp.encode(bean));
	}
}
