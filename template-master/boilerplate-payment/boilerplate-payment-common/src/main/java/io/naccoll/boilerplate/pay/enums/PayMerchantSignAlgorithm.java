package io.naccoll.boilerplate.pay.enums;

import cn.hutool.crypto.digest.HmacAlgorithm;
import io.naccoll.boilerplate.core.enums.DisplayEnum;
import io.naccoll.boilerplate.core.enums.EnumHelper;

/**
 * The enum Pay merchant sign algorithm.
 */
public enum PayMerchantSignAlgorithm implements DisplayEnum {

	/**
	 * Hmac md 5 pay merchant sign algorithm.
	 */
	HmacMD5(1, "HmacMD5", HmacAlgorithm.HmacMD5),

	/**
	 * Hmac sha 1 pay merchant sign algorithm.
	 */
	HmacSHA1(2, "HmacSHA1", HmacAlgorithm.HmacSHA1),

	/**
	 * Hmac sha 256 pay merchant sign algorithm.
	 */
	HmacSHA256(3, "HmacSHA256", HmacAlgorithm.HmacSHA256),

	/**
	 * Hmac sha 384 pay merchant sign algorithm.
	 */
	HmacSHA384(4, "HmacSHA384", HmacAlgorithm.HmacSHA384),

	/**
	 * Hmac sha 512 pay merchant sign algorithm.
	 */
	HmacSHA512(5, "HmacSHA512", HmacAlgorithm.HmacSHA512),

	/**
	 * HmacSM3算法实现，需要BouncyCastle库支持
	 */
	HmacSM3(6, "HmacSM3", HmacAlgorithm.HmacSM3),

	/**
	 * SM4 CMAC模式实现，需要BouncyCastle库支持
	 */
	SM4CMAC(7, "SM4CMAC", HmacAlgorithm.SM4CMAC);

	private final int id;

	private final String name;

	private final HmacAlgorithm algorithm;

	PayMerchantSignAlgorithm(int id, String name, HmacAlgorithm algorithm) {
		this.id = id;
		this.name = name;
		this.algorithm = algorithm;
	}

	/**
	 * From id pay merchant sign algorithm.
	 * @param id the id
	 * @return the pay merchant sign algorithm
	 */
	public static PayMerchantSignAlgorithm fromId(int id) {
		return EnumHelper.fromId(PayMerchantSignAlgorithm.class, id);
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
	 * Gets algorithm.
	 * @return the algorithm
	 */
	public HmacAlgorithm getAlgorithm() {
		return algorithm;
	}

}
