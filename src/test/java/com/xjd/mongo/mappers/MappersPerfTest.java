package com.xjd.mongo.mappers;

import org.bson.Document;

import com.xjd.mongo.mappers5.AddressBean;
import com.xjd.mongo.mappers5.TestBean;

/**
 * @author elvis.xu
 * @since 2018-09-08 21:45
 */
public class MappersPerfTest {
	public static void main(String[] args) {
		TestBean bean = new TestBean();
		bean.setName("xxx");
		bean.setAge(20);
		bean.setAddress(new AddressBean().setCode("1001"));
		bean.getAds().add(new AddressBean().setCode("1003"));

		int times = 10000;
		for (int j = 0; j < 10; j++) {
			System.out.println("------------");
			{
				long start = System.currentTimeMillis();
				for (int i = 0; i < times; i++) {
					Document document = Mappers.encode(bean, TestBean.class);
				}
				long cost = System.currentTimeMillis() - start;
				print(times, cost);
			}
			{
				long start = System.currentTimeMillis();
				for (int i = 0; i < times; i++) {
					Document document = Mappers.encode(bean, TestBean.class, false);
				}
				long cost = System.currentTimeMillis() - start;
				print(times, cost);
			}
			{
				long start = System.currentTimeMillis();
				for (int i = 0; i < times; i++) {
					Document document = Mappers.encode(bean, TestBean.class, true, (t, c) -> {
						c.add(t.getId());
						c.add(t.getName());
						c.add(t.getAddress().getCode());
						c.add(t.getAds().get(10).getCode());
					});
				}
				long cost = System.currentTimeMillis() - start;
				print(times, cost);
			}
		}


	}

	public static void print(int times, long cost) {
		System.out.println(times + "/" + cost + " ms - " + (times / (cost == 0 ? 1: cost)) + " /ms");
	}
}
