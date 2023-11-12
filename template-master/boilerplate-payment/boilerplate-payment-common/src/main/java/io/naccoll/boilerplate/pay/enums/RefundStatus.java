package io.naccoll.boilerplate.pay.enums;

import io.naccoll.boilerplate.core.enums.DisplayEnum;
import io.naccoll.boilerplate.core.enums.EnumHelper;

/**
 * The enum Refund status.
 */
public enum RefundStatus implements DisplayEnum {

	/**
	 * Error refund status.
	 */
	ERROR(-1, "退款失败"),
	/**
	 * Refunding refund status.
	 */
	REFUNDING(0, "退款中"),
	/**
	 * Success refund status.
	 */
	SUCCESS(1, "退款成功"),;

	private final Integer id;

	private final String name;

	RefundStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * From id refund status.
	 * @param id the id
	 * @return the refund status
	 */
	public static RefundStatus fromId(Integer id) {
		return EnumHelper.fromId(RefundStatus.class, id);
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
