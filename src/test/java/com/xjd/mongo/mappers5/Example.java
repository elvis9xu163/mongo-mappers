package com.xjd.mongo.mappers5;


import org.bson.Document;

import com.xjd.mongo.mappers4.MappedDocument;
import com.xjd.mongo.mappers4.Mapper;
import com.xjd.mongo.mappers4.Mappers;

/**
 * @author elvis.xu
 * @since 2018-09-06 18:52
 */
public class Example {
	public static void main(String[] args) {
		com.xjd.mongo.mappers4.Mapper<TestBean> mapper = Mappers.of(TestBean.class);
		TestBean bean = new TestBean();
		Document document = new Document();
	}

	public static void toDocument(com.xjd.mongo.mappers4.Mapper<TestBean> mapper, TestBean bean) {
		{
			// normal
			Document document = mapper.encode(bean);
		}
		{
			// non null
			Document document = mapper.encode(bean, false);
		}
		{
			// only include properties
			Document document = mapper.encode(bean, (p, c) -> {
				c.add(p.getId());
				c.add(p.getAddress().getAddress());
			}, true);
		}
		{
			// except proeprties
			Document document = mapper.encode(bean, true, (p, c) -> {
				c.add(p.getId());
				c.add(p.getAddress().getAddress());
			});
		}
	}

	public static void toBean(com.xjd.mongo.mappers4.Mapper<TestBean> mapper, Document document) {
		TestBean bean = mapper.decode(document);
	}


	public static void toDocumentCustom(com.xjd.mongo.mappers4.Mapper<TestBean> mapper, TestBean bean) {
		{
			MappedDocument<TestBean> document = mapper.doc();
			document.append(TestBean::getId, bean.getId()); // {_id: bean.id}
		}
		{
			MappedDocument<TestBean> document = mapper.doc();
			document.append(TestBean::getAge, new Document("$gt", 10).append("$lt", 30)).append(p -> p.getAddress().getCode(), bean.getAddress().getCode());
			// {a: {$gt: 10, $lt: 30}, "d.code": bean.address.code}
		}
		{
			MappedDocument<TestBean> document = mapper.doc();
			document.append(TestBean::getAddress, mapper.doc().exec(()->{Mappers.indent(1);})
					.append(p -> p.getAddress().getAddress(), bean.getAddress().getAddress())
					.append(p -> p.getAddress().getCode(), bean.getAddress().getCode())
					.exec(() -> {Mappers.indentReset();}));
			// {d: {address: bean.address.address, code: bean.address.code}}
		}
	}

	public static void toBeanCustom(Mapper<TestBean> mapper, Document document) {
		TestBean bean = new TestBean();
		MappedDocument<TestBean> doc = mapper.doc(document);
		bean.setId(doc.getString(TestBean::getId));
		bean.setName(doc.getString(TestBean::getName));
		bean.setAge(doc.getInteger(TestBean::getAge));

		Document addressDocument = doc.get(TestBean::getAddress, Document.class);
		AddressBean address = new AddressBean();

		// first way
		MappedDocument<TestBean> addressDoc = mapper.doc(addressDocument);
		Mappers.indent(1);
		address.setAddress(addressDoc.getString(p -> p.getAddress().getAddress()));
		address.setCode(addressDoc.getString(p -> p.getAddress().getCode()));
		Mappers.indentReset();

		// second way recommanded
		MappedDocument<AddressBean> addressDoc2 = Mappers.of(AddressBean.class).doc(addressDocument);
		address.setAddress(addressDoc2.getString(p -> p.getAddress()));
		address.setCode(addressDoc2.getString(AddressBean::getCode));

	}
}
