package io.naccoll.boilerplate.customer.enums;

import io.naccoll.boilerplate.core.enums.DisplayEnum;
import io.naccoll.boilerplate.core.enums.EnumHelper;

/**
 * 接入的第三方类型
 */
public enum ThirdType implements DisplayEnum {

	/**
	 * 微信公众号
	 */
	WX_MP(1, "微信公众号"),

	/**
	 * 微信小程序
	 */
	WX_MINIAPP(2, "微信小程序"),

	;

	private final Integer id;

	private final String name;

	ThirdType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public static ThirdType fromId(int id) {
		return EnumHelper.fromId(ThirdType.class, id);
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

}
