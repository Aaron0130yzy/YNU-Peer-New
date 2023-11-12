package io.naccoll.boilerplate.customer.enums;

import io.naccoll.boilerplate.core.enums.DisplayEnum;
import io.naccoll.boilerplate.core.enums.EnumHelper;

/**
 * 绑定状态
 */
public enum BindStatus implements DisplayEnum {

	/**
	 * 绑定
	 */
	BIND(1, "绑定"),
	/**
	 * 解绑
	 */
	UNBIND(2, "解绑"),

	;

	private final Integer id;

	private final String name;

	BindStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public static BindStatus fromId(int id) {
		return EnumHelper.fromId(BindStatus.class, id);
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
