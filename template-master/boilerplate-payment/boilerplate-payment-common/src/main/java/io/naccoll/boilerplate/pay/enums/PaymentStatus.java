package io.naccoll.boilerplate.pay.enums;

import io.naccoll.boilerplate.core.enums.DisplayEnum;
import io.naccoll.boilerplate.core.enums.EnumHelper;

/**
 * The enum Payment status.
 */
public enum PaymentStatus implements DisplayEnum {

	/**
	 * Cancel payment status.
	 */
	CANCEL(-1, "已取消"),
	/**
	 * Wait pay payment status.
	 */
	WAIT_PAY(0, "待支付"),
	/**
	 * Pay payment status.
	 */
	PAY(1, "已支付"),;

	private final Integer id;

	private final String name;

	PaymentStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * From id payment status.
	 * @param id the id
	 * @return the payment status
	 */
	public static PaymentStatus fromId(Integer id) {
		return EnumHelper.fromId(PaymentStatus.class, id);
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
