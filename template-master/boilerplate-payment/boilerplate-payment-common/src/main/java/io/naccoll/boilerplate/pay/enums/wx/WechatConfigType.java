package io.naccoll.boilerplate.pay.enums.wx;

import io.naccoll.boilerplate.core.enums.DisplayEnum;
import io.naccoll.boilerplate.core.enums.EnumHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Wechat config type.
 */
@Getter
@AllArgsConstructor
public enum WechatConfigType implements DisplayEnum {

	/**
	 * 直连商户
	 */
	DIRECT(1, "直连商户"),
	/**
	 * 服务商户
	 */
	PROVIDER(2, "服务商户"),
	/**
	 * 特约商户
	 */
	SUB(3, "特约商户");

	private final int id;

	private final String name;

	/**
	 * From id wechat config type.
	 * @param id the id
	 * @return the wechat config type
	 */
	public static WechatConfigType fromId(int id) {
		return EnumHelper.fromId(WechatConfigType.class, id);
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
