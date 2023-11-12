package io.naccoll.boilerplate.pay.enums;

import io.naccoll.boilerplate.core.enums.DisplayEnum;
import io.naccoll.boilerplate.core.enums.EnumHelper;

/**
 * The enum Pay way.
 */
public enum PayWay implements DisplayEnum {

	/**
	 * Wechat pay way.
	 */
	WECHAT(1, "微信"),
	/**
	 * Alipay pay way.
	 */
	ALIPAY(2, "支付宝"),
	/**
	 * Unknown pay way.
	 */
	UNKNOWN(0, "未知"),

	;

	private final Integer id;

	private final String name;

	PayWay(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * From id pay way.
	 * @param id the id
	 * @return the pay way
	 */
	public static PayWay fromId(Integer id) {
		return EnumHelper.fromId(PayWay.class, id);
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
