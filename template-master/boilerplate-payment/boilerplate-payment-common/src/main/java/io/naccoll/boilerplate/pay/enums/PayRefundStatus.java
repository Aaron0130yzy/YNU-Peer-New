package io.naccoll.boilerplate.pay.enums;

import io.naccoll.boilerplate.core.enums.DisplayEnum;
import io.naccoll.boilerplate.core.enums.EnumHelper;

/**
 * The enum Pay refund status.
 */
public enum PayRefundStatus implements DisplayEnum {

	/**
	 * No pay refund status.
	 */
	NO(0, "未退款"),
	/**
	 * Part refund pay refund status.
	 */
	PART_REFUND(1, "部分退款"),
	/**
	 * Complete refund pay refund status.
	 */
	COMPLETE_REFUND(2, "全额退款"),;

	private final Integer id;

	private final String name;

	PayRefundStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * From id pay refund status.
	 * @param id the id
	 * @return the pay refund status
	 */
	public static PayRefundStatus fromId(Integer id) {
		return EnumHelper.fromId(PayRefundStatus.class, id);
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
