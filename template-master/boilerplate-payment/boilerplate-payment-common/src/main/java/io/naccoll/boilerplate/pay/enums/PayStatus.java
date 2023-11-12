package io.naccoll.boilerplate.pay.enums;

import io.naccoll.boilerplate.core.enums.DisplayEnum;
import io.naccoll.boilerplate.core.enums.EnumHelper;

/**
 * The enum Pay status.
 */
public enum PayStatus implements DisplayEnum {

	/**
	 * Other error pay status.
	 */
	OTHER_ERROR(-2, "支付失败"),
	/**
	 * Cancel pay status.
	 */
	CANCEL(-1, "已撤单"),
	/**
	 * Wait pay pay status.
	 */
	WAIT_PAY(0, "待支付"),
	/**
	 * Pay pay status.
	 */
	PAY(1, "已支付"),;

	private final Integer id;

	private final String name;

	PayStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * From id pay status.
	 * @param id the id
	 * @return the pay status
	 */
	public static PayStatus fromId(Integer id) {
		return EnumHelper.fromId(PayStatus.class, id);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Integer getId() {
		return id;
	}

}
