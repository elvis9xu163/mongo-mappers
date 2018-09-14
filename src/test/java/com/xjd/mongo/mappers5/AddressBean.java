package com.xjd.mongo.mappers5;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author elvis.xu
 * @since 2018-09-06 18:56
 */
@Getter
@Setter
@Accessors(chain = true)
public class AddressBean {
	private String address;
	private String code;
}
