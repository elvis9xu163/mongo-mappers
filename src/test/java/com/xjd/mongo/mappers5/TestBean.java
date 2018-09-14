package com.xjd.mongo.mappers5;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.xjd.mongo.mappers.annotation.Entity;
import com.xjd.mongo.mappers.annotation.Property;


/**
 * @author elvis.xu
 * @since 2018-09-06 18:53
 */
@Getter
@Setter
@Entity("test")
public class TestBean extends BaseBean {
	@Property("n")
	private String name;
	@Property("a")
	private Integer age;
	@Property("d")
	private AddressBean address;

	@Property(componentClass = AddressBean.class)
	private List<AddressBean> ads = new LinkedList<>();

	private AddressBean[] ads2 = new AddressBean[3];

	private int[] is = new int[]{0, 1, 3, 100};

	private double[] ds = new double[]{1.1D, 2.3d};
}
