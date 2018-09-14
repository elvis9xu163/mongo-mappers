package com.xjd.mongo.mappers5;

import lombok.Getter;
import lombok.Setter;

import com.xjd.mongo.mappers5.annotation.Prop;


/**
 * @author elvis.xu
 * @since 2018-09-06 18:55
 */
@Getter
@Setter
public class BaseBean {
	@Prop("_id")
	private String id;
}
