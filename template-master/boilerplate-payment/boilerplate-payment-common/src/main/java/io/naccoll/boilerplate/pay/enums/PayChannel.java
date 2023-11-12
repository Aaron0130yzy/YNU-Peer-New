package io.naccoll.boilerplate.pay.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.naccoll.boilerplate.core.enums.DisplayEnum;
import io.naccoll.boilerplate.core.enums.EnumHelper;

/**
 * The enum Pay channel.
 */
public enum PayChannel implements DisplayEnum {

	/**
	 * 微信JSAPI支付
	 */
	WECHAT_JSAPI(1, "微信JSAPI支付", PayWay.WECHAT),

	/**
	 * Wechat native pay channel.
	 */
	WECHAT_NATIVE(2, "微信NATIVE支付", PayWay.WECHAT),

	/**
	 * Wechat micro pay channel.
	 */
	WECHAT_MICRO(3, "微信付款码支付", PayWay.WECHAT),

	/**
	 * Alipay miniapp pay channel.
	 */
	ALIPAY_MINIAPP(8, "支付宝小程序支付", PayWay.ALIPAY),

	/**
	 * Alipay wap pay channel.
	 */
	ALIPAY_WAP(9, "支付宝手机网站支付", PayWay.ALIPAY),

	/**
	 * Alipay sweep pay channel.
	 */
	ALIPAY_SWEEP(10, "支付宝扫码支付", PayWay.ALIPAY),

	/**
	 * Alipay micro pay channel.
	 */
	ALIPAY_MICRO(11, "支付宝付款码支付", PayWay.ALIPAY),;

	private final Integer id;

	private final String name;

	private final PayWay payWay;

	PayChannel(Integer id, String name, PayWay payWay) {
		this.id = id;
		this.name = name;
		this.payWay = payWay;
	}

	/**
	 * From id pay channel.
	 * @param id the id
	 * @return the pay channel
	 */
	public static PayChannel fromId(Integer id) {
		return EnumHelper.fromId(PayChannel.class, id);
	}

	/**
	 * Wechat list.
	 * @return the list
	 */
	public static List<PayChannel> wechat() {
		return Arrays.stream(values())
			.filter(i -> Objects.equals(i.getPayWay(), PayWay.WECHAT))
			.collect(Collectors.toList());
	}

	/**
	 * Alipay list.
	 * @return the list
	 */
	public static List<PayChannel> alipay() {
		return Arrays.stream(values())
			.filter(i -> Objects.equals(i.getPayWay(), PayWay.ALIPAY))
			.collect(Collectors.toList());
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Gets pay way.
	 * @return the pay way
	 */
	public PayWay getPayWay() {
		return payWay;
	}

}
