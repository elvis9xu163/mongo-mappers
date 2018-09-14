package com.xjd.mongo.mappers;

import org.bson.Document;

import com.xjd.mongo.mappers5.AddressBean;
import com.xjd.mongo.mappers5.TestBean;

/**
 * @author elvis.xu
 * @since 2018-09-08 21:45
 */
public class MapDocumentTest {
	public static void main(String[] args) {
		MapDocument<TestBean> doc = Mappers.mapDocument(TestBean.class);

		doc.exec(() -> {
			System.out.println("HELLO");
		}).exec(() -> {
			System.out.println("WORLD");
		}).append(TestBean::getName, "NAME")
				.append(TestBean::getAddress, new AddressBean().setAddress("xxxx"))
				.append(t -> {t.getAds().get(10).getAddress();}, new Document());
		System.out.println(doc);
	}
}
