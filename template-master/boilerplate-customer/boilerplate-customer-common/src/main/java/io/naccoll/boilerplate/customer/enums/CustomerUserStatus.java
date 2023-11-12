package io.naccoll.boilerplate.customer.enums;

import io.naccoll.boilerplate.core.enums.DisplayEnum;

/**
 * 用户状态
 */
public enum CustomerUserStatus implements DisplayEnum {

	/**
	 * 禁用
	 */
	DISABLE(0, "禁用"),
	/**
	 * 解绑
	 */
	ENABLE(1, "启用"),

	;

	private final Integer id;

	private final String name;

	CustomerUserStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
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
