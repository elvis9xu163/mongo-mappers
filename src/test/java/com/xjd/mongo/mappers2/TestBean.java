package com.xjd.mongo.mappers2;

import java.util.List;

import lombok.Getter;

import com.xjd.mongo.mappers3.annotation.Property;

/**
 * @author elvis.xu
 * @since 2018-09-05 17:26
 */
@Getter
public class TestBean {
	@Property("_id")
	private String id;

	@Property("n")
	private String name;

	@Property("age")
	private int age;

	@Property("age2")
	private Integer age2;

	private Address address;

	@Property("addrs")
	private List<Address> addressList;

	@Getter
	public static class Address {
		@Property("format_address")
		private String format;


	}
}
