package com.xjd.mongo.mappers;

import org.bson.Document;

import com.xjd.mongo.mappers5.AddressBean;
import com.xjd.mongo.mappers5.TestBean;

/**
 * @author elvis.xu
 * @since 2018-09-08 21:45
 */
public class MappersTest {
	public static void main(String[] args) {
		TestBean bean = new TestBean();
		bean.setName("xxx");
		bean.setAge(20);
		bean.setAddress(new AddressBean().setCode("1001"));
		bean.getAds().add(new AddressBean().setCode("1003"));

		{
			Document document = Mappers.encode(bean, TestBean.class, true);
			System.out.println(document);
		}
		{
			Document document = Mappers.encode(bean, TestBean.class, true, (t, c) -> {
				c.add(t.getId());
				c.add(t.getName());
				c.add(t.getAddress().getCode());
				c.add(t.getAds().get(10).getCode());
			});
			System.out.println(document);
		}
		{
			Document document = Mappers.encode(bean, TestBean.class, false);
			System.out.println(document);
		}
	}
}
